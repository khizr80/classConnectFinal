package com.springmvcapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Student implements Login {
    private String userName;
    private String password;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String checkSql = "SELECT COUNT(*) FROM student WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{username, password}, Integer.class);
        if (count > 0)
            return "student";
        else
            return "incorrect";

    }

    public String createUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String sql = "INSERT INTO student (username, password) VALUES (?, ?)";

        try {
            jdbcTemplate.update(sql, username, password);
            return "registered";
        } catch (DataIntegrityViolationException e) {
            // Handle the error when the username already exists
            return "userAlreadyExists";
        }
    }

    public List<Course> getCoursesByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT course_id, courseName FROM course_students WHERE student_id = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) ->
                new Course(rs.getString("course_id"), rs.getString("courseName"))
        );
    }
    public String insertMessage(String senderId, String receiverId, String role, String messageText) {
        if (senderId.isEmpty() || receiverId.isEmpty() || role.isEmpty() || messageText.isEmpty()) {
            return "fieldEmpty";
        }

        // SQL query to insert the message
        String sql = "INSERT INTO message (sender, receiver, date, text, roll) VALUES (?, ?, ?, ?, ?)";

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        System.out.println(receiverId+"new");
        try {
            // Execute the update
            jdbcTemplate.update(sql, senderId, receiverId, formattedDate, messageText, role);

            return "messageSent";
        } catch (DataIntegrityViolationException e) {
            // Handle the error when there is a constraint violation, e.g., non-existent foreign keys
            return "databaseError";
        }
    }
    public String getTeacherUsernameByCourseId(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            return null;
        }

        String sql = "SELECT teacherUsername FROM course WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{courseId}, String.class);
    }
}

// Getter for userName

