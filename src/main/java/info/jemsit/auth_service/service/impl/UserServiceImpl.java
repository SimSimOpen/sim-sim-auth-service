package info.jemsit.auth_service.service.impl;

import info.jemsit.auth_service.controller.UserDetailsRequestDTO;
import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.mapper.AuthMapper;
import info.jemsit.auth_service.service.UserService;
import info.jemsit.common.UserContext;
import info.jemsit.common.clients.auth.ProfileServiceClient;
import info.jemsit.common.dto.request.auth.ProfileRequestDTO;
import info.jemsit.common.dto.response.auth.UserDetailsResponseDTO;
import info.jemsit.common.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final AuthMapper authMapper;
    private final ProfileServiceClient profileServiceClient;

    @Override
    public UserDetailsResponseDTO getUserDetails() {
        var user = userDAO.
                findByToken(UserContext.getUserToken()).
                orElseThrow(
                        () -> new UserException("User not found by token")
                );
        return authMapper.toDto(user);
    }

    @Override
    public UserDetailsResponseDTO updateUserDetails(UserDetailsRequestDTO userDetails) {
        var user = userDAO.
                findByToken(UserContext.getUserToken()).
                orElseThrow(
                        () -> new UserException("User not found by token")
                );
        user.setUsername(userDetails.username());
        user.setEmail(userDetails.email());

        Pair<String, String> fullName = userDetails.fullName().split(" ").length > 1 ?
                Pair.of(userDetails.fullName().split(" ")[0], userDetails.fullName().split(" ")[1]) :
                Pair.of(userDetails.fullName(), "");

        ProfileRequestDTO profileRequest = new ProfileRequestDTO(
                user.getProfileId(),
                user.getId(),
                null,
                userDetails.phoneNumber(),
                fullName.getFirst(),
                fullName.getSecond(),
                null,
                userDetails.description()
        );
        UserContext.setUserToken("Bearer " + user.getTokens().getLast().getToken());
        profileServiceClient.updateProfile(user.getProfileId(), profileRequest);
        return authMapper.toDto(userDAO.update(user));
    }
}
