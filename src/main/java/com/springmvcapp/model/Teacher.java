package com.springmvcapp.model;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
                "WHERE (sender = ? AND receiver = ? AND roll = ?) OR (sender = ? AND receiver = ? AND roll = ?)";

        return jdbcTemplate.queryForList(sql, sender, receiver, role, receiver, sender, role);
    }
    public List<Map<String, Object>> getStreamMessages(String sender, String receiver, String role) {

        String sql = "SELECT sender, receiver, text, `date`, roll " +
                "FROM message " +
                "WHERE receiver = ? AND roll = ?";

        return jdbcTemplate.queryForList(sql, receiver, role);
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

    public List<String> getStudentsByCourseAndTeacher(String courseId, String teacherUsername) {
        String sql = "SELECT student_id FROM course_students WHERE course_id = ? AND teacherUsername = ?";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, courseId, teacherUsername);

        List<String> students = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            students.add((String) row.get("student_id"));
        }
        return students;
    }
    public void insertMarks(List<String> studentUsernames, String evaluationName, String weightage, String totalMarks, String teacherUsername, String courseId, String obtainedMarks) {
        String sql = "INSERT INTO Marks (studentUsername, evaluationName, weightage, totalMarks, teacherUsername, courseID, obtainedMarks) VALUES (?, ?, ?, ?, ?, ?, ?)";

        for (int i = 0; i < studentUsernames.size(); i++) {
            String studentUsername = studentUsernames.get(i);
            jdbcTemplate.update(sql, studentUsername, evaluationName, weightage, totalMarks, teacherUsername, courseId, obtainedMarks);
        }
    }
    public List<String> getDistinctEvaluationNames(String courseId, String teacherUsername) {
        String sql = "SELECT DISTINCT evaluationName FROM Marks WHERE courseID = ? AND teacherUsername = ?";
        List<String> evaluationNames = jdbcTemplate.queryForList(sql, String.class, courseId, teacherUsername);
        return evaluationNames;
    }
    public List<Map<String, Object>> getMarksByCourseAndEvaluation(String courseId, String evaluationName, String teacherUsername) {
        String sql = "SELECT studentUsername, obtainedMarks, totalMarks FROM Marks WHERE courseId = ? AND evaluationName = ? AND teacherUsername = ?";
        List<Map<String, Object>> marksList = jdbcTemplate.queryForList(sql, courseId, evaluationName, teacherUsername);
        return marksList;
    }
    public void saveObtainedMarks(String courseId, String evaluationName, String studentUsername, String obtainedMarks) {
        String sql = "UPDATE Marks SET obtainedMarks = ? WHERE courseId = ? AND evaluationName = ? AND studentUsername = ?";
        jdbcTemplate.update(sql, obtainedMarks, courseId, evaluationName, studentUsername);
    }
    public List<Map<String, Object>> getStudentsByCourseId(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT student_id FROM course_students WHERE course_id = ?";
        return jdbcTemplate.query(sql, new Object[]{courseId}, (rs, rowNum) -> {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("studentId", rs.getString("student_id"));
            return studentMap;
        });
    }
    public void insertAttendance(String studentId, String teacherUsername, String courseId, String attendanceStatus, LocalDate date) {
        String sql = "INSERT INTO attendance (student_id, teacher_username, course_id, date, status) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status)";

        // Convert LocalDate to java.sql.Date (without time component)
        Date sqlDate = Date.valueOf(date);

        jdbcTemplate.update(sql, studentId, teacherUsername, courseId, sqlDate, attendanceStatus);
    }
}


