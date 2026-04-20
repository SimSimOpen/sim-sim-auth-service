package info.jemsit.auth_service.service.impl;

import info.jemsit.auth_service.config.JwtService;
import info.jemsit.auth_service.data.dao.TokenDAO;
import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.data.model.Token;
import info.jemsit.auth_service.data.model.User;
import info.jemsit.auth_service.mapper.AuthMapper;
import info.jemsit.auth_service.service.AuthService;
import info.jemsit.common.UserContext;
import info.jemsit.common.clients.auth.ProfileServiceClient;
import info.jemsit.common.data.enums.Roles;
import info.jemsit.common.dto.request.auth.AuthenticationRequestDTO;
import info.jemsit.common.dto.request.auth.ProfileRequestDTO;
import info.jemsit.common.dto.request.auth.RegisterRequestDTO;
import info.jemsit.common.dto.response.auth.AuthenticationResponseDTO;
import info.jemsit.common.dto.response.auth.ProfileResponseDTO;
import info.jemsit.common.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserDAO userDAO;
    private final TokenDAO tokenDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final ProfileServiceClient profileServiceClient;

    @Transactional
    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO) {
        var user = userDAO.findByUsername(requestDTO.username()).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + requestDTO.username()));
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new UserException("Password is incorrect");
        }
        var tokenModel = crateTokenModel(user);
        user.addToken(tokenModel);
        user.setUpdatedAt(LocalDateTime.now());
        UserContext.setUserToken("Bearer " + tokenModel.getToken());
        var userProfile = profileServiceClient.getProfileById(user.getProfileId());
        return authMapper.toDTOWithProfile(userDAO.update(user), tokenModel, userProfile);
    }

    @Override
    public void registerClient(RegisterRequestDTO request) {
        var userExists = userDAO.findByUsernameOrEmail(request.username(), request.email());
        if (userExists.isPresent()) {
            throw new UserException("User with given username or email already exists");
        }
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setAuthorities(List.of(Roles.CLIENT));
        var savedUser = userDAO.save(newUser);
        var resp = createProfileForUser(savedUser.getId(), null, null, null, null, null);
        savedUser.setProfileId(resp.id());
        userDAO.update(savedUser);

    }

    @Override
    public void registerAgent(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setAuthorities(List.of(Roles.AGENT));
        var savedUser = userDAO.save(newUser);
        var resp = createProfileForUser(savedUser.getId(), null, null, null, null, null);
        savedUser.setProfileId(resp.id());
        userDAO.update(savedUser);
    }

    @Override
    public void registerAdmin(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setAuthorities(List.of(Roles.ADMIN));
        var savedUser = userDAO.save(newUser);
        var resp = createProfileForUser(savedUser.getId(), null, null, null, null, null);
        savedUser.setProfileId(resp.id());
        userDAO.save(savedUser);
    }


    @Override
    public AuthenticationResponseDTO authenticateWithOtp(AuthenticationRequestDTO requestDTO) {
        var user = userDAO.findByUsername(requestDTO.username());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(requestDTO.username());
            newUser.setAuthorities(List.of(Roles.CLIENT));
            var savedUser = userDAO.save(newUser);
            var tokenModel = crateTokenModel(savedUser);
            savedUser.addToken(tokenModel);
            savedUser.setUpdatedAt(LocalDateTime.now());
            var resp = createProfileForUser(savedUser.getId(), null, null, null, null, null);
            savedUser.setProfileId(resp.id());
            return authMapper.toDTO(userDAO.update(savedUser), tokenModel);
        } else {
            var tokenModel = crateTokenModel(user.get());
            user.get().addToken(tokenModel);
            user.get().setUpdatedAt(LocalDateTime.now());
            return authMapper.toDTO(userDAO.update(user.get()), tokenModel);
        }
    }

    private Token crateTokenModel(User user) {
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        Token tokenModel = new Token();
        tokenModel.setToken(token);
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setUser(user);
        return tokenModel;
    }

    private ProfileResponseDTO createProfileForUser(Long userId, String phoneNumber, String firstName, String lastName, String profileImageUrl, String description) {
        ProfileRequestDTO request = new ProfileRequestDTO(
                null,
                userId,
                null,
                phoneNumber,
                firstName,
                lastName,
                profileImageUrl,
                description
        );
        return profileServiceClient.createProfile(request);
    }

}
