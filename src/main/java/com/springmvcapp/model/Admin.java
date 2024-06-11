package com.springmvcapp.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class Admin {
public Admin()
{

}

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public String authenticate(String username, String password) {
    String checkSql = "SELECT COUNT(*) FROM admin WHERE username = ? AND password = ?";
    int count = jdbcTemplate.queryForObject(checkSql, new Object[] {username, password}, Integer.class);
    if(count>0)
        return "admin";
    else
        return "incorrect";
        }
    public String addTeacher(String username, String password) {
        String sql = "INSERT INTO teacher (username, password) VALUES (?, ?)";

        try {
            jdbcTemplate.update(sql, username, password);
            return "teacherAdded";
        } catch (DataIntegrityViolationException e) {
            return "userAlreadyExists";
        }
    }

    public List<Map<String, Object>> getAllCourses() {
        String sql = "SELECT course_id, course_name, teacherUsername, status FROM course";
        return jdbcTemplate.queryForList(sql);
    }
    public void updateCourseStatus(String courseId, String teacherUsername, String status) {
        String sql = "UPDATE course SET status = ? WHERE course_id = ? AND teacherUsername = ?";
        jdbcTemplate.update(sql, status, courseId, teacherUsername);
    }
    public List<String> getUnprocessedStudentUsernames() {
        String sql = "SELECT username FROM student WHERE approved = '0'";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    public List<String> getAllStudentUsernames() {
        String sql = "SELECT username FROM student";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    public List<String> getAllTeacherUsernames() {
        String sql = "SELECT username FROM teacher";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public void updateStatusByUsername(String username, String approved) {
        String sql = "UPDATE student SET approved = ? WHERE username = ?";
        jdbcTemplate.update(sql, approved, username);
    }
    public boolean deleteUser(String username, String userType) {
        String deleteSql;
        if ("teacher".equalsIgnoreCase(userType)) {
            deleteSql = "DELETE FROM teacher WHERE username = ?";
        } else if ("student".equalsIgnoreCase(userType)) {
            deleteSql = "DELETE FROM student WHERE username = ?";
        } else {
            throw new IllegalArgumentException("Invalid userType: " + userType);
        }

        int rowsAffected = jdbcTemplate.update(deleteSql, username);
        return rowsAffected > 0;
    }
    public String offerCourse( int semester, String courseName) {
        
        String sql = "INSERT INTO course ( course_name, teacherUsername, status, semester) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(sql, courseName,"def", 1, semester);
        return "success";
    }

    }

