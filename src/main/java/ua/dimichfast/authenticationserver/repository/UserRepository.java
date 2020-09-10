package ua.dimichfast.authenticationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dimichfast.authenticationserver.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailEqualsOrUsernameEquals(String username,String email);
}
