package info.jemsit.auth_service.data.repository;

import info.jemsit.auth_service.data.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}