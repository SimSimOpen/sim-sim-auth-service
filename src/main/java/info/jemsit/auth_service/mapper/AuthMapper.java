package info.jemsit.auth_service.mapper;

import info.jemsit.auth_service.data.model.Token;
import info.jemsit.auth_service.data.model.User;
import info.jemsit.common.data.enums.Roles;
import info.jemsit.common.dto.response.auth.AuthenticationResponseDTO;
import info.jemsit.common.dto.response.auth.UserDetailsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "token", source = "token.token")
    @Mapping(target = "refreshToken", source = "token.refreshToken")
    @Mapping(target = "authorities", source = "user.authorities", qualifiedByName = "mapAuthoritiesToRoles")
    AuthenticationResponseDTO toDTO(User user, Token token);

    @Mapping(target = "roles", source = "user.authorities", qualifiedByName = "mapAuthoritiesToRoles")
    UserDetailsResponseDTO toDto(User user);


    @Named("mapAuthoritiesToRoles")
    default List<Roles> mapAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return null;
        }
        return authorities.stream()
                .map(authority -> Roles.valueOf(authority.getAuthority()))
                .collect(Collectors.toList());
    }
}
