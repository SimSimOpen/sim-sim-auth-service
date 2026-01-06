package info.jemsit.auth_service.data.repository;

import info.jemsit.auth_service.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}