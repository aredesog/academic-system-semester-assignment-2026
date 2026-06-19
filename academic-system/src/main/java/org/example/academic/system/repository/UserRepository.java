package org.example.academic.system.repository;

import org.example.academic.system.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
