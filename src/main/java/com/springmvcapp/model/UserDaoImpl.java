package com.springmvcapp.model;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl  {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int createUser(String username, String password) {
        String sql = "INSERT INTO logincredential (username, password) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, username, password);
            return 0;
        } catch (DataIntegrityViolationException e) {
            // Handle the error when the username already exists
            return 1;
        }
    }

    public String authenticate(String username, String password)
    {
        String checkSql = "SELECT COUNT(*) FROM logincredential WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[] {username, password}, Integer.class);
    if(count>0)
        return "success";
    else
        return "fail";

    }
}
