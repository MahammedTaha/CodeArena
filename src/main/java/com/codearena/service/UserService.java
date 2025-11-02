package com.codearena.service;

import com.codearena.model.User;
import com.codearena.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User u) {
        try {
            // Check if username already exists
            User existingUser = repo.findByUsername(u.getUsername());
            if (existingUser != null) {
                throw new RuntimeException("Username already exists");
            }

            // Check if email already exists
            User existingEmail = repo.findByEmail(u.getEmail());
            if (existingEmail != null) {
                throw new RuntimeException("Email already exists");
            }

            u.setPassword(encoder.encode(u.getPassword()));
            return repo.save(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public void awardPoints(Long uid, int addPoints) {
        User u = repo.findById(uid);
        if (u != null) {
            int newPoints = u.getPoints() + addPoints;
            int newCompleted = u.getCompletedProjects() + 1;
            repo.updatePointsAndCompleted(uid, newPoints, newCompleted);
        }
    }

    public User login(String username, String rawPassword) {
        try {
            User user = repo.findByUsername(username);
            if (user != null && encoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getLeaderboard() {
        return repo.findAll(); // Already sorted by points DESC
    }
}