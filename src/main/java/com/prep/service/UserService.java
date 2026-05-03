package com.prep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prep.entity.User;
import com.prep.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    // 🔥 REGISTER (CLEAN VERSION)
    public User register(User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
            throw new RuntimeException("Email and password are required");
        }

        // normalize email
        user.setEmail(user.getEmail().trim().toLowerCase());

        // prevent duplicate
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // role handling
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }

        System.out.println("Saving user: " + user.getEmail() + " ROLE: " + user.getRole());

        return repo.save(user);
    }

    // 🔥 LOGIN
    public User login(String email, String password) {

        if (email == null || password == null) {
            throw new RuntimeException("Email or password is missing");
        }

        email = email.trim().toLowerCase();
        password = password.trim();

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // ✅ Get user by ID
    public User getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🔥 Update user
    public User updateUser(Long id, User newUser) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());

        if (newUser.getRole() != null && !newUser.getRole().isEmpty()) {
            user.setRole(newUser.getRole().toUpperCase());
        }

        return repo.save(user);
    }

    // 🔥 Delete user
    public void deleteUser(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Cannot delete admin user");
        }

        repo.deleteById(id);
    }
}