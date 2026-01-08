package info.jemsit.auth_service.service;

import info.jemsit.common.dto.request.AuthenticationRequestDTO;
import info.jemsit.common.dto.request.AuthenticationResponseDTO;
import info.jemsit.common.dto.request.RegisterRequestDTO;

public interface AuthService {
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO);

    void registerClient(RegisterRequestDTO request);

    void registerAgent(RegisterRequestDTO request);
}
