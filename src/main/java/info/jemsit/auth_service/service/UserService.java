package info.jemsit.auth_service.service;

import info.jemsit.common.dto.response.auth.UserDetailsResponseDTO;

public interface UserService {
    UserDetailsResponseDTO getUserDetails();
}
