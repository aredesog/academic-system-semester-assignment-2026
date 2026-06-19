package org.example.academic.system.repository;

import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TxtUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public TxtUserRepository() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], Role.valueOf(parts[2])));
                }
            }
        } catch (IOException e) {
            // In a real application, you'd want to handle this more gracefully
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
