package org.example.academic.system.controller;

import org.example.academic.system.model.User;
import org.example.academic.system.security.AuthenticationService;

public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // Injeta o serviço de autenticação
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Autentica o usuário com as credenciais fornecidas e retorna o usuário logado
    public User login(String username, String password) {
        return authenticationService.authenticate(username, password);
    }

    // Encerra a sessão do usuário autenticado
    public void logout() {
        authenticationService.logout();
    }
}
