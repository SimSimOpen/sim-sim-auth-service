package info.jemsit.auth_service.service.impl;

import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.mapper.AuthMapper;
import info.jemsit.auth_service.service.UserService;
import info.jemsit.common.UserContext;
import info.jemsit.common.dto.response.auth.UserDetailsResponseDTO;
import info.jemsit.common.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final AuthMapper authMapper;
    @Override
    public UserDetailsResponseDTO getUserDetails() {
        var user = userDAO.
                findByToken(UserContext.getUserToken()).
                orElseThrow(
                        ()-> new UserException("User not found by token")
                );
        return authMapper.toDto(user);
    }
}
