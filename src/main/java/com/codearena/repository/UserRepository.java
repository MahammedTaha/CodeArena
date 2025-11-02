package com.codearena.repository;

import com.codearena.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<User> rowMapper = (rs, rn) -> {
        User u = new User();
        u.setU_id(rs.getLong("U_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setCompletedProjects(rs.getInt("completedProjects"));
        u.setPoints(rs.getInt("points"));
        u.setRole(rs.getString("role"));
        return u;
    };

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User findByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            List<User> list = jdbc.query(sql, rowMapper, username);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            List<User> list = jdbc.query(sql, rowMapper, email);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE U_id = ?";
            List<User> list = jdbc.query(sql, rowMapper, id);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAll() {
        try {
            return jdbc.query("SELECT * FROM users WHERE role = 'ROLE_CODER' ORDER BY points DESC;", rowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public User save(User u) {
        try {
            String sql = "INSERT INTO users (username, email, password, completedProjects, points, role) VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            int result = jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setInt(4, u.getCompletedProjects());
                ps.setInt(5, u.getPoints());
                ps.setString(6, u.getRole());
                return ps;
            }, keyHolder);

            if (result > 0 && keyHolder.getKey() != null) {
                u.setU_id(keyHolder.getKey().longValue());
                return u;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updatePointsAndCompleted(Long id, int newPoints, int newCompleted) {
        try {
            String sql = "UPDATE users SET points = ?, completedProjects = ? WHERE U_id = ?";
            return jdbc.update(sql, newPoints, newCompleted, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}