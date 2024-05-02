package com.springmvcapp.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class Admin implements Login {


public Admin()
{

}

private String userName;
private String password;
    @Autowired
    private JdbcTemplate jdbcTemplate;
@Override
        public String authenticate(String username, String password) {
    if(username.isEmpty() || password.isEmpty())
        return "fieldEmpty";
    String checkSql = "SELECT COUNT(*) FROM admin WHERE username = ? AND password = ?";
    int count = jdbcTemplate.queryForObject(checkSql, new Object[] {username, password}, Integer.class);
    if(count>0)
        return "admin";
    else
        return "incorrect";
        }
    public String addTeacher(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String sql = "INSERT INTO teacher (username, password) VALUES (?, ?)";

        try {
            jdbcTemplate.update(sql, username, password);
            return "teacherAdded";
        } catch (DataIntegrityViolationException e) {
            return "userAlreadyExists";
        }
    }
    public boolean checkTeacherExists(String teacherUsername) {
        // Write your logic here to check if the teacher exists in the database
        // You can execute a query to check if the teacher with the provided username exists
        String sql = "SELECT COUNT(*) FROM teacher WHERE username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, teacherUsername);
        return count > 0;
    }

    public String offerCourse(int courseId, int semester, String teacherUsername, String courseName) {
        // Validate input parameters
        if (courseName.isEmpty() || teacherUsername.isEmpty()) {
            return "fieldsEmpty";
        }

        // Check if the teacher exists (You might want to implement this method in Admin class)
        // Assuming you have a method to check if the teacher exists
        boolean teacherExists = checkTeacherExists(teacherUsername);
        if (!teacherExists) {
            return "teacherNotFound";
        }

        // Save course details to the database
        String sql = "INSERT INTO course (course_id, course_name, teacherUsername, status, semester) VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, courseId, courseName, teacherUsername, 1, semester);
            return "courseOffered";
        } catch (DataIntegrityViolationException e) {
            return "courseAlreadyExists";
        }
    }

    public String deleteTeacher(String username) {
        if(username.isEmpty()) {
            return "fieldEmpty";
        }
        String sql = "DELETE FROM teacher WHERE username = ?";

            int rowsAffected = jdbcTemplate.update(sql, username);
            if (rowsAffected > 0)
            {
                return "userDeleted";
            }
            else
            {
                return "userNotFound";
            }

    }
    public String deleteStudent(String username) {
        if(username.isEmpty()) {
            return "fieldEmpty";
        }
        String sql = "DELETE FROM student WHERE username = ?";

        int rowsAffected = jdbcTemplate.update(sql, username);
        if (rowsAffected > 0)
        {
            return "userDeleted";
        }
        else
        {
            return "userNotFound";
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
    public void updateStatusByUsername(String username, String approved) {
        String sql = "UPDATE student SET approved = ? WHERE username = ?";
        jdbcTemplate.update(sql, approved, username);
    }
    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
    }

