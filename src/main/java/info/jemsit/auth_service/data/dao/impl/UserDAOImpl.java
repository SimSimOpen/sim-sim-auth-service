package info.jemsit.auth_service.data.dao.impl;

import info.jemsit.auth_service.data.dao.UserDAO;
import info.jemsit.auth_service.data.model.User;
import info.jemsit.auth_service.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        log.info("Updating user: {}", user.getUsername());
        return userRepository.saveAndFlush(user);
    }
}
