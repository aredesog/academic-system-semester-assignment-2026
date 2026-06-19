package org.example.academic.system.security;

import org.example.academic.system.exception.AuthenticationException;
import org.example.academic.system.model.User;
import org.example.academic.system.repository.UserRepository;

import java.util.Optional;

public class AuthenticationService {

    private final UserRepository userRepository;
    private final Session session = Session.getInstance();

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            throw new AuthenticationException("Invalid credentials");
        }

        User user = userOptional.get();
        session.login(user);
        return user;
    }
}
