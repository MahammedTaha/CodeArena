package com.codearena.controller;

import com.codearena.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "*")
public class SessionController {

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("No active session");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Return minimal user info for security
            User userInfo = new User();
            userInfo.setUsername(user.getUsername());
            userInfo.setRole(user.getRole());
            userInfo.setPoints(user.getPoints());
            userInfo.setCompletedProjects(user.getCompletedProjects());
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
}