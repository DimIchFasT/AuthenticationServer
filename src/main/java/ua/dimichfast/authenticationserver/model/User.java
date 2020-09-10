package ua.dimichfast.authenticationserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity implements UserDetails {

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @OneToOne(fetch = FetchType.EAGER)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(Status.ACTIVE);
    }
}