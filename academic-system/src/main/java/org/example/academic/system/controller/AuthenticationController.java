package org.example.academic.system.controller;

import org.example.academic.system.model.User;
import org.example.academic.system.security.AuthenticationService;

public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public User login(String username, String password) {
        return authenticationService.authenticate(username, password);
    }

    public void logout() {
        authenticationService.logout();
    }
}
