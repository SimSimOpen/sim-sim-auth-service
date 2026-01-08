package info.jemsit.auth_service.service.impl;

import info.jemsit.auth_service.config.JwtService;
import info.jemsit.auth_service.data.dao.TokenDAO;
import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.data.model.Token;
import info.jemsit.auth_service.data.model.User;
import info.jemsit.auth_service.mapper.AuthMapper;
import info.jemsit.auth_service.service.AuthService;
import info.jemsit.common.data.enums.Roles;
import info.jemsit.common.dto.request.AuthenticationRequestDTO;
import info.jemsit.common.dto.request.AuthenticationResponseDTO;
import info.jemsit.common.dto.request.RegisterRequestDTO;
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

    @Transactional
    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO) {
        var user = userDAO.findByUsername(requestDTO.username()).orElseThrow(()->new UsernameNotFoundException("User not found with username: " + requestDTO.username()));
        if(!passwordEncoder.matches(requestDTO.password(), user.getPassword())){
            throw new UserException("Password is incorrect");
        }
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        Token tokenModel = new Token();
        tokenModel.setToken(token);
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setUser(user);
        user.addToken(tokenModel);
        user.setUpdatedAt(LocalDateTime.now());
        return authMapper.toDTO(userDAO.update(user), tokenModel);
    }

    @Override
    public void registerClient(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setAuthorities(List.of(Roles.CLIENT));
        userDAO.save(newUser);
    }

    @Override
    public void registerAgent(RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setAuthorities(List.of(Roles.AGENT));
        userDAO.save(newUser);
    }
}
