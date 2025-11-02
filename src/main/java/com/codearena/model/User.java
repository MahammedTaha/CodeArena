package com.codearena.model;

public class User {
    private Long U_id;
    private String username;
    private String email;
    private String password;
    private int completedProjects;
    private int points;
    private String role;

    // Constructors
    public User() {}

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.completedProjects = 0;
        this.points = 0;
    }

    // Getters and Setters
    public Long getU_id() { return U_id; }
    public void setU_id(Long U_id) { this.U_id = U_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getCompletedProjects() { return completedProjects; }
    public void setCompletedProjects(int completedProjects) { this.completedProjects = completedProjects; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}