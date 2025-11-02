package com.codearena.service;

import com.codearena.model.Project;
import com.codearena.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) {
        this.repo = repo;
    }

    public List<Project> getAllProjects() {
        return repo.findAll();
    }

    public List<Project> getOpenProjects() {
        return repo.findByStatus("OPEN");
    }

    public Project getProjectById(Long id) {
        return repo.findById(id);
    }

    public Project createProject(Project project) {
        return repo.save(project);
    }

    public boolean assignProject(Long projectId, String username) {
        return repo.updateStatus(projectId, "ASSIGNED", username) > 0;
    }

    public boolean completeProject(Long projectId) {
        return repo.updateStatus(projectId, "COMPLETED", null) > 0;
    }
}