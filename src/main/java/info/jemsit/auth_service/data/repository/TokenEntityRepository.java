package info.jemsit.auth_service.data.repository;

import info.jemsit.auth_service.data.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenEntityRepository extends JpaRepository<TokenEntity, Long> {
}