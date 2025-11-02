package com.codearena.controller;

import com.codearena.model.Project;
import com.codearena.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/open")
    public ResponseEntity<List<Project>> getOpenProjects() {
        try {
            List<Project> projects = projectService.getOpenProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            Project created = projectService.createProject(project);
            if (created != null) {
                return ResponseEntity.ok(created);
            } else {
                return ResponseEntity.badRequest().body("Failed to create project");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating project: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<?> assignProject(@PathVariable Long id, @RequestBody AssignRequest request) {
        try {
            boolean success = projectService.assignProject(id, request.getUsername());
            if (success) {
                return ResponseEntity.ok("Project assigned successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to assign project");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error assigning project: " + e.getMessage());
        }
    }

    static class AssignRequest {
        private String username;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}