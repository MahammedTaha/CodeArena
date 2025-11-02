package com.codearena.service;

import com.codearena.model.Quest;
import com.codearena.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestService {
    private final QuestRepository repo;

    public QuestService(QuestRepository repo) {
        this.repo = repo;
    }

    public List<Quest> getAllQuests() {
        try {
            System.out.println("QuestService: Getting all quests...");
            List<Quest> quests = repo.findAll();
            System.out.println("QuestService: Found " + quests.size() + " quests");
            return quests;
        } catch (Exception e) {
            System.err.println("ERROR in QuestService.getAllQuests(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load quests: " + e.getMessage(), e);
        }
    }

    public Quest getQuestById(Long id) {
        return repo.findById(id);
    }

    public Quest createQuest(Quest quest) {
        try {
            System.out.println("QuestService: Creating quest - " + quest.getTitle());
            return repo.save(quest);
        } catch (Exception e) {
            System.err.println("ERROR in QuestService.createQuest(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create quest: " + e.getMessage(), e);
        }
    }

    public List<Quest> getQuestsByUser(String username) {
        try {
            return repo.findByPostedBy(username);
        } catch (Exception e) {
            System.err.println("ERROR in QuestService.getQuestsByUser(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}