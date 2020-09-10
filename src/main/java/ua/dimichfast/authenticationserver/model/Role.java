package ua.dimichfast.authenticationserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
@Data
public class Role extends AbstractEntity {

    private String name;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
