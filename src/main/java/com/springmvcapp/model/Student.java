package com.springmvcapp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class Student implements Login{
    private String userName;
    private String password;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public String authenticate(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            return "fieldEmpty";
        }
        String checkSql = "SELECT COUNT(*) FROM student WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[] {username, password}, Integer.class);
        if(count>0)
            return "success";
        else
            return "incorrect";

    }
    public String createUser(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
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
