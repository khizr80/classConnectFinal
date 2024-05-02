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
public class Student implements Login {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String authenticate(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String checkSql = "SELECT COUNT(*) FROM student WHERE username = ? AND password = ? AND approved = 'accept'";
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
    public String getTeacherUsernameByCourseId(String courseId)
    {
        if (courseId == null || courseId.isEmpty()) {
            return null;
        }

        String sql = "SELECT teacherUsername FROM course WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{courseId}, String.class);
    }
    public List<Map<String, Object>> getMessagesBySenderReceiverAndRole(String sender, String receiver, String role) {

        String sql = "SELECT sender, receiver, text, `date`, roll " +
                "FROM message " +
                "WHERE (sender = ? AND receiver = ? AND roll = ?) OR (sender = ? AND receiver = ? AND roll = ?)";

        return jdbcTemplate.queryForList(sql, sender, receiver, role, receiver, sender, role);
    }
    public List<Map<String, Object>> getStreamMessages(String sender, String receiver, String role) {

        String sql = "SELECT sender, receiver, text, `date`, roll " +
                "FROM message " +
                "WHERE receiver = ? AND roll = ?";

        return jdbcTemplate.queryForList(sql, receiver, role);
    }

    public List<Map<String, Object>> getTranscriptByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = "SELECT courseName, Grade, courseID FROM transcript WHERE student_id = ?";
        return jdbcTemplate.queryForList(sql, username);
    }
    public List<Map<String, Object>> getMarks(String studentUsername, String teacherUsername, String courseId) {
        String sql = "SELECT studentUsername, teacherUsername, courseId, evaluationName, totalMarks, obtainedMarks, weightage " +
                "FROM Marks " +
                "WHERE studentUsername = ? AND teacherUsername = ? AND courseId = ?";
        return jdbcTemplate.queryForList(sql, studentUsername, teacherUsername, courseId);
    }
    public List<Map<String, Object>> getAllActiveCourses() {
        String sql = "SELECT * FROM course WHERE status = 1";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> getAttendanceByStudentAndCourseAndTeacher(String studentUsername, String courseId, String teacherUsername) {
        String sql = "SELECT * FROM attendance WHERE student_id = ? AND course_id = ? AND teacher_username = ?";
        return jdbcTemplate.queryForList(sql, studentUsername, courseId, teacherUsername);
    }
    public void registerCourse(String courseId, String teacherUsername, String courseName, String studentId) {
        String sql = "INSERT INTO course_students (course_id, student_id, courseName, teacherUsername) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, courseId, studentId, courseName, teacherUsername);
    }

}

