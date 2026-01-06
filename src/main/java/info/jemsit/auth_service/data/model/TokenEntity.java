package info.jemsit.auth_service.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenEntity extends BaseEntity {
    @Column(name = "token", unique = true)
    private String token;
    @Column(name = "refresh_token", unique = true)
    private String refreshToken;
}
