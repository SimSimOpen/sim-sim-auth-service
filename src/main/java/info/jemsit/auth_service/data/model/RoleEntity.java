package info.jemsit.auth_service.data.model;

import info.jemsit.common.data.enums.Roles;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Column(name = "authorities")
    @Enumerated(EnumType.STRING)
    private List<Roles> authorities;
}
