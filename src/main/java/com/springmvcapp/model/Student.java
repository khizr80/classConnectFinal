package com.springmvcapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    //    public List<String> getCoursesByUsername(String username) {
//        if (username == null || username.isEmpty()) {
//            return null;  // Return null or an empty list depending on your error handling
//        }
//        String sql = "SELECT course_id FROM course_students WHERE student_id  = ?";
//        return jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> rs.getString("course_id"));
//    }
//}
    public List<Course> getCoursesByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT course_id, courseName FROM course_students WHERE student_id = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) ->
                new Course(rs.getString("course_id"), rs.getString("courseName"))
        );
    }

}

// Getter for userName

