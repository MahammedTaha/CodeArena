package com.codearena.controller;

import com.codearena.model.User;
import com.codearena.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final UserService userService;

    public AuthRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u) {
        try {
            System.out.println("Registration attempt for username: " + u.getUsername());

            // Basic validation
            if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Username is required", null));
            }
            if (u.getPassword() == null || u.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Password is required", null));
            }
            if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email is required", null));
            }

            //u.setRole("ROLE_CODER");
            User registeredUser = userService.register(u);

            if (registeredUser != null) {
                System.out.println("Registration successful for user: " + registeredUser.getUsername());
                return ResponseEntity.ok(new MessageResponse("Registration successful", registeredUser));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Registration failed", null));
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpSession session) {
        try {
            System.out.println("=== LOGIN ATTEMPT ===");

            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

            if (user != null) {
                // Set session attributes
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());

                // CRITICAL: Manually set authentication in security context
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("✅ Login successful for: " + user.getUsername());
                System.out.println("Session ID: " + session.getId());

                return ResponseEntity.ok(new MessageResponse("Login successful", user));
            } else {
                return ResponseEntity.status(401).body(new MessageResponse("Invalid credentials", null));
            }
        } catch (Exception e) {
            System.err.println("💥 LOGIN ERROR: " + e.getMessage());
            return ResponseEntity.internalServerError().body(new MessageResponse("Login failed", null));
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(new MessageResponse("Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse("Logout completed", null));
        }
    }

    public static class MessageResponse {
        private String message;
        private User user;

        public MessageResponse(String message, User user) {
            this.message = message;
            this.user = user;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
    }

    @GetMapping("/debug-session")
    public ResponseEntity<?> debugSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("userInSession", user != null);
        if (user != null) {
            response.put("username", user.getUsername());
            response.put("userId", user.getU_id());
        }
        response.put("sessionCreationTime", new Date(session.getCreationTime()));
        response.put("lastAccessedTime", new Date(session.getLastAccessedTime()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Return safe user data (without password)
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("points", user.getPoints());
            userData.put("completedProjects", user.getCompletedProjects());
            userData.put("role", user.getRole());
            userData.put("userId", user.getU_id());
            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.status(401).body("No user in session");
        }
    }

}