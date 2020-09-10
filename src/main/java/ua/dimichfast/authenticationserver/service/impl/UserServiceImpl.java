package ua.dimichfast.authenticationserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.dimichfast.authenticationserver.model.Status;
import ua.dimichfast.authenticationserver.model.User;
import ua.dimichfast.authenticationserver.repository.RoleRepository;
import ua.dimichfast.authenticationserver.repository.UserRepository;
import ua.dimichfast.authenticationserver.service.UserService;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER"));
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        log.info("IN getAll - {} users found", users.size());
        return users;
    }

    @Override
    public User findByUsername(String username) {
        User userFromDB = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        log.info("IN findByUsername - user {} found by username: {}", userFromDB, username);
        return userFromDB;
    }

    @Override
    public User findByEmail(String email) {
        User userFromDB = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        log.info("IN findByEmail - user {} found by email: {}", userFromDB, email);
        return userFromDB;
    }

    @Override
    public User findById(Long id) {
        User userFromDB = userRepository.findById(id).orElse(null);
        if (userFromDB == null) {
            log.warn("IN findById - no user found by id: {},", id);
            return null;
        }
        log.info("IN findById - user {} found by id: {}", userFromDB, id);
        return userFromDB;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);

    }
}
