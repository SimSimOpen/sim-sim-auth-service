package info.jemsit.auth_service.service;

import info.jemsit.common.dto.request.AuthenticationRequestDTO;

public interface AuthService {
    void authenticate(AuthenticationRequestDTO requestDTO);
}
