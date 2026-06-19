package org.example.academic.system.model;

import java.util.Objects;

public class User {

    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this.username = Objects.requireNonNull(username, "username cannot be null");
        this.password = Objects.requireNonNull(password, "password cannot be null");
        this.role = Objects.requireNonNull(role, "role cannot be null");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof User user)) {
            return false;
        }

        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}
