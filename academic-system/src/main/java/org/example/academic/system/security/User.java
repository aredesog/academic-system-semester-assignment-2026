package org.example.academic.system.security;

import lombok.Getter;
import lombok.NonNull;
import org.example.academic.system.model.Role;

import java.util.Objects;

/**
 * Representa um usuário autenticável do sistema acadêmico.
 *
 * Equality é definida pelo username (TUS-2382).
 * A senha NUNCA deve ser logada (AC6 de US-2366, TUS-2391).
 */
@Getter
public class User {

    private final String username;
    private final String password;
    private final Role role;

    public User(@NonNull String username,
                @NonNull String password,
                @NonNull Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Dois User com o mesmo username são considerados iguais (TUS-2382).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User other)) return false;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Senha omitida intencionalmente para evitar vazamento em logs (AC6 US-2366).
     */
    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}
