package org.example.academic.system.security;

import org.example.academic.system.model.User;

public class Session {
    private static Session instance;
    private User authenticatedUser;

    // Construtor privado para garantir que só exista uma instância (padrão Singleton)
    private Session() {
    }

    // Retorna a única instância da sessão, criando-a na primeira chamada
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Registra o usuário autenticado na sessão atual
    public void login(User user) {
        this.authenticatedUser = user;
    }

    // Retorna o usuário autenticado, ou null se ninguém estiver logado
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    // Remove o usuário da sessão, efetivando o logout
    public void logout() {
        this.authenticatedUser = null;
    }

    // Verifica se existe um usuário autenticado na sessão
    public boolean isAuthenticated() {
        return authenticatedUser != null;
    }
}
