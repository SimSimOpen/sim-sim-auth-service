package info.jemsit.auth_service.service;

import info.jemsit.auth_service.controller.UserDetailsRequestDTO;
import info.jemsit.common.dto.response.auth.UserDetailsResponseDTO;

public interface UserService {
    UserDetailsResponseDTO getUserDetails();

    UserDetailsResponseDTO updateUserDetails(UserDetailsRequestDTO userDetails);
}
