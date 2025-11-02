package com.codearena.model;

public class Quest {
    private Long Q_id;
    private String title;
    private String description;
    private String difficulty;
    private int points_reward;
    private String posted_by; // Add this field

    // Constructors
    public Quest() {}

    public Quest(String title, String description, String difficulty, int points_reward, String posted_by) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.points_reward = points_reward;
        this.posted_by = posted_by;
    }

    // Getters and Setters
    public Long getQ_id() { return Q_id; }
    public void setQ_id(Long Q_id) { this.Q_id = Q_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getPoints_reward() { return points_reward; }
    public void setPoints_reward(int points_reward) { this.points_reward = points_reward; }

    public String getPosted_by() { return posted_by; }
    public void setPosted_by(String posted_by) { this.posted_by = posted_by; }
}