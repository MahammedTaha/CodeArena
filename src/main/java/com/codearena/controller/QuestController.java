package com.codearena.controller;

import com.codearena.model.Quest;
import com.codearena.service.QuestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/quests")
@CrossOrigin(origins = "*")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping
    public ResponseEntity<List<Quest>> getAllQuests() {
        try {
            List<Quest> quests = questService.getAllQuests();
            return ResponseEntity.ok(quests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-quests")
    public ResponseEntity<?> getMyQuests(@RequestParam String username) {
        try {
            List<Quest> quests = questService.getQuestsByUser(username);
            return ResponseEntity.ok(quests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error loading user quests: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuest(@RequestBody Quest quest) {
        try {
            Quest created = questService.createQuest(quest);
            if (created != null) {
                return ResponseEntity.ok(created);
            } else {
                return ResponseEntity.badRequest().body("Failed to create quest");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating quest: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        try {
            // Test database connection
            int count = questService.getAllQuests().size();
            return ResponseEntity.ok("Quest service is healthy. Found " + count + " quests.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Quest service error: " + e.getMessage());
        }
    }
}