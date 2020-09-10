package ua.dimichfast.authenticationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dimichfast.authenticationserver.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
