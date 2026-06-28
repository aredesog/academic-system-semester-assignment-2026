package org.example.academic.system.model;

import java.util.Objects;

public class User {

    private final String username;
    private final String password;
    private final Role role;

    // Cria um usuário garantindo que nenhum campo seja nulo
    public User(String username, String password, Role role) {
        this.username = Objects.requireNonNull(username, "username cannot be null");
        this.password = Objects.requireNonNull(password, "password cannot be null");
        this.role = Objects.requireNonNull(role, "role cannot be null");
    }

    // Retorna o nome de usuário
    public String getUsername() {
        return username;
    }

    // Retorna a senha do usuário
    public String getPassword() {
        return password;
    }

    // Retorna o papel (Role) do usuário (ADMIN ou PROFESSOR)
    public Role getRole() {
        return role;
    }

    // Dois usuários são iguais se tiverem o mesmo username
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

    // Hash baseado no username para consistência com equals
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    // Representação textual do usuário mostrando username e role
    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}
