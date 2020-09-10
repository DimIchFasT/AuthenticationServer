package ua.dimichfast.authenticationserver.service;

import ua.dimichfast.authenticationserver.model.User;

import java.util.List;

public interface UserService {
    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);
}
