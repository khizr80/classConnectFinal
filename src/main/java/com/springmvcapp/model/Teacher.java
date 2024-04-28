package com.springmvcapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class Teacher implements Login {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public String authenticate(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String checkSql = "SELECT COUNT(*) FROM teacher WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[] {username, password}, Integer.class);
        if(count > 0) {
            return "teacher";
        } else {
            return "incorrect";
        }
    }
    public List<Course> getCoursesByTeacherUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ArrayList<>();
        }
        // Update the SQL query to filter by teacherUsername instead of student_id
        String sql = "SELECT course_id, course_name FROM course WHERE teacherUsername = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) ->
                new Course(rs.getString("course_id"), rs.getString("course_name"))
        );
    }
    public List<Map<String, Object>> getMessagesBySenderReceiverAndRole(String sender, String receiver, String role) {

        String sql = "SELECT sender, receiver, text, `date`, roll " +
                "FROM message " +
                "WHERE receiver = '" + receiver + "' AND sender = '" + sender + "' AND roll = '" + role + "'";

        return jdbcTemplate.queryForList(sql);
    }
    public String insertMessage(String senderId, String receiverId, String role, String messageText) {

        String sql = "INSERT INTO message (sender, receiver, date, text, roll) VALUES (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        try {
            jdbcTemplate.update(sql, senderId, receiverId, formattedDate, messageText, role);
            return "success";
        } catch (DataIntegrityViolationException e) {
            return "teacher";
        }
    }
}


