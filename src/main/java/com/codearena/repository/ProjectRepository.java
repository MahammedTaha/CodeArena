package com.codearena.repository;

import com.codearena.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class ProjectRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Project> rowMapper = (rs, rn) -> {
        Project p = new Project();
        p.setP_id(rs.getLong("P_id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setBudget(rs.getDouble("budget"));
        p.setStatus(rs.getString("status_0"));
        p.setPosted_by(rs.getString("posted_by"));
        p.setAssigned_to(rs.getString("assigned_to"));
        return p;
    };

    public ProjectRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Project> findAll() {
        try {
            return jdbc.query("SELECT * FROM projects ORDER BY P_id DESC", rowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Project> findByStatus(String status) {
        try {
            String sql = "SELECT * FROM projects WHERE status_0 = ? ORDER BY P_id DESC";
            return jdbc.query(sql, rowMapper, status);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Project findById(Long id) {
        try {
            String sql = "SELECT * FROM projects WHERE P_id = ?";
            List<Project> list = jdbc.query(sql, rowMapper, id);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Project save(Project p) {
        try {
            String sql = "INSERT INTO projects (title, description, budget, status_0, posted_by, assigned_to) VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            int result = jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, p.getTitle());
                ps.setString(2, p.getDescription());
                ps.setDouble(3, p.getBudget());
                ps.setString(4, p.getStatus());
                ps.setString(5, p.getPosted_by());
                ps.setString(6, p.getAssigned_to());
                return ps;
            }, keyHolder);

            if (result > 0 && keyHolder.getKey() != null) {
                p.setP_id(keyHolder.getKey().longValue());
                return p;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updateStatus(Long id, String status, String assigned_to) {
        try {
            String sql = "UPDATE projects SET status_0 = ?, assigned_to = ? WHERE P_id = ?";
            return jdbc.update(sql, status, assigned_to, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}