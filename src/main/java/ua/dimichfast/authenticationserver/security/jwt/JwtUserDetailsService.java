package ua.dimichfast.authenticationserver.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.dimichfast.authenticationserver.model.User;
import ua.dimichfast.authenticationserver.repository.UserRepository;

@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("ПОПЫТКА ДОСТАТЬ ЮЗЕРА");
        User user = userRepository.findByEmailEqualsOrUsernameEquals(usernameOrEmail, usernameOrEmail).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        System.out.println(user.toString());
        return user;
    }
}
