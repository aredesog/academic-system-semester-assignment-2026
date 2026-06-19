package org.example.academic.system.security;

import lombok.Getter;
import org.example.academic.system.model.User;

@Getter
public class Session {
    private static Session instance;
    private User authenticatedUser;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(User user) {
        this.authenticatedUser = user;
    }

    public void logout() {
        this.authenticatedUser = null;
    }

    public boolean isAuthenticated() {
        return authenticatedUser != null;
    }
}
