package org.example.academic.system.security;

import org.example.academic.system.exception.AuthorizationException;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;

import java.util.Arrays;

public class AuthorizationService {

    private final Session session = Session.getInstance();

    public void authorize(Role... requiredRoles) {
        User authenticatedUser = session.getAuthenticatedUser();

        if (authenticatedUser == null) {
            throw new AuthorizationException("No user is authenticated");
        }

        if (Arrays.stream(requiredRoles).noneMatch(role -> role == authenticatedUser.getRole())) {
            throw new AuthorizationException("User does not have the required role");
        }
    }
}
