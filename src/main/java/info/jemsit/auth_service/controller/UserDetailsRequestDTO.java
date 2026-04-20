package info.jemsit.auth_service.controller;

public record UserDetailsRequestDTO(
        String username,
        String email,
        String fullName,
        String phoneNumber,
        String description
) {
}
