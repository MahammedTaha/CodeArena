package com.codearena.repository;

import com.codearena.model.Quest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class QuestRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Quest> rowMapper = (rs, rn) -> {
        Quest q = new Quest();
        q.setQ_id(rs.getLong("Q_id"));
        q.setTitle(rs.getString("title"));
        q.setDescription(rs.getString("description"));
        q.setDifficulty(rs.getString("difficulty"));
        q.setPoints_reward(rs.getInt("points_reward"));
        return q;
    };

    public QuestRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Quest> findAll() {
        try {
            System.out.println("QuestRepository: Executing findAll query...");
            List<Quest> quests = jdbc.query("SELECT * FROM quests ORDER BY Q_id DESC", rowMapper);
            System.out.println("QuestRepository: Query completed, found " + quests.size() + " quests");
            return quests;
        } catch (Exception e) {
            System.err.println("ERROR in QuestRepository.findAll(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database error while fetching quests: " + e.getMessage(), e);
        }
    }

    public Quest findById(Long id) {
        try {
            String sql = "SELECT * FROM quests WHERE Q_id = ?";
            List<Quest> list = jdbc.query(sql, rowMapper, id);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            System.err.println("ERROR in QuestRepository.findById(): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Quest save(Quest q) {
        try {
            String sql = "INSERT INTO quests (title, description, difficulty, points_reward, posted_by) VALUES (?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            int result = jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, q.getTitle());
                ps.setString(2, q.getDescription());
                ps.setString(3, q.getDifficulty());
                ps.setInt(4, q.getPoints_reward());
                ps.setString(5, q.getPosted_by() != null ? q.getPosted_by() : "unknown");
                return ps;
            }, keyHolder);

            if (result > 0 && keyHolder.getKey() != null) {
                q.setQ_id(keyHolder.getKey().longValue());
                return q;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Quest> findByPostedBy(String postedBy) {
        try {
            String sql = "SELECT * FROM quests WHERE posted_by = ? ORDER BY Q_id DESC";
            return jdbc.query(sql, rowMapper, postedBy);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}