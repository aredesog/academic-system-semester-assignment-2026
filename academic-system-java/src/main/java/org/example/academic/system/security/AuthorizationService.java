package org.example.academic.system.security;

import org.example.academic.system.exception.AuthorizationException;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final Session session = Session.getInstance();

    // Verifica se o usuário autenticado possui um dos papéis exigidos; lança exceção se não autorizado
    public void authorize(Role... requiredRoles) {
        User authenticatedUser = session.getAuthenticatedUser();

        if (authenticatedUser == null) {
            logger.warn("Authorization failed: no authenticated user");
            throw new AuthorizationException("No user is authenticated");
        }

        if (Arrays.stream(requiredRoles).noneMatch(role -> role == authenticatedUser.getRole())) {
            logger.warn("Authorization failed: user '{}' with role '{}' attempted operation requiring roles: {}",
                    authenticatedUser.getUsername(), authenticatedUser.getRole(), Arrays.toString(requiredRoles));
            throw new AuthorizationException("User does not have the required role");
        }

        logger.debug("Authorization granted for user '{}' with role '{}'",
                authenticatedUser.getUsername(), authenticatedUser.getRole());
    }
}
