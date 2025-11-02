package com.codearena.model;

import java.util.Objects;

public class Project {
    private Long P_id;
    private String title;
    private String description;
    private Double budget;
    private String status;
    private String posted_by;
    private String assigned_to;

    public Project() {}

    public Project(String title, String description, Double budget, String posted_by) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.status = "OPEN";
        this.posted_by = posted_by;
    }

    // Getters and Setters
    public Long getP_id() { return P_id; }
    public void setP_id(Long P_id) { this.P_id = P_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = Objects.requireNonNullElse(status, "OPEN");
    }

    public String getPosted_by() { return posted_by; }
    public void setPosted_by(String posted_by) { this.posted_by = posted_by; }

    public String getAssigned_to() { return assigned_to; }
    public void setAssigned_to(String assigned_to) { this.assigned_to = assigned_to; }
}