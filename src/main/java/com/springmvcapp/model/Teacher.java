package com.springmvcapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


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

}
