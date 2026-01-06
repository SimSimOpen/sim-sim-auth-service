package info.jemsit.auth_service.data.repository;

import info.jemsit.auth_service.data.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
}