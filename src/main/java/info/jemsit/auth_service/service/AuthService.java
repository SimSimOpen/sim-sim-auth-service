package info.jemsit.auth_service.service;

import info.jemsit.common.dto.request.auth.AuthenticationRequestDTO;
import info.jemsit.common.dto.request.auth.RegisterRequestDTO;
import info.jemsit.common.dto.response.auth.AuthenticationResponseDTO;

public interface AuthService {
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO);

    void registerClient(RegisterRequestDTO request);

    void registerAgent(RegisterRequestDTO request);

    void registerAdmin(RegisterRequestDTO request);
}
