package info.jemsit.auth_service.config;

import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.service.AuthService;
import info.jemsit.common.dto.request.auth.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAdminUser implements ApplicationRunner {

    private final AuthService authService;
    private final UserDAO userDAO;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userDAO.findByUsername("admin").ifPresentOrElse(
                user -> {
                    // Admin user already exists, do nothing
                },
                () -> {
                    // Admin user does not exist, create it
                    authService.registerAdmin(new RegisterRequestDTO(
                            "admin",
                            "Seydi272127@",
                            "jimishukurow@gmail.com",
                            "",
                            ""
                    ));
                }
        );

    }
}
