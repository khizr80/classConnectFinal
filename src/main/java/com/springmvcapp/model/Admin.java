package com.springmvcapp.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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

