package org.example.academic.system.security;

import org.example.academic.system.exception.AuthenticationException;
import org.example.academic.system.model.User;
import org.example.academic.system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final Session session = Session.getInstance();

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            logger.warn("Failed authentication attempt for username: {}", username);
            throw new AuthenticationException("Invalid credentials");
        }

        User user = userOptional.get();
        session.login(user);
        logger.info("User '{}' authenticated successfully with role: {}", username, user.getRole());
        return user;
    }

    public void logout() {
        User user = session.getAuthenticatedUser();
        if (user != null) {
            logger.info("User '{}' logged out", user.getUsername());
        }
        session.logout();
    }
}
