package info.jemsit.auth_service.data.dao;

import info.jemsit.auth_service.data.model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> findByUsername(String username);
    User save(User user);

    User update(User user);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
