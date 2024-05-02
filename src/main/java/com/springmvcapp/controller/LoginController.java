package com.springmvcapp.controller;

import com.springmvcapp.model.Admin;
import com.springmvcapp.model.Teacher;
import com.springmvcapp.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class LoginController {

    private final Admin admin;
    private final Teacher teacher;
    private final Student student;

    public LoginController(Admin admin, Teacher teacher, Student student) {
        this.admin = admin;
        this.teacher = teacher;
        this.student = student;
    }

    @GetMapping("/login")
    public String handleLogin(@RequestParam("id") String id, @RequestParam("pass") String password, @RequestParam("value") String userType, HttpServletResponse response) {
        String result;

        switch (userType) {
            case "1": // Student creation
                result = student.createUser(id, password);
                break;
            case "2": // Student authentication
                result = student.authenticate(id, password);
                if ("student".equals(result)) {
                    createCookie(response, id);
                }
                break;
            case "3": // Teacher authentication
                result = teacher.authenticate(id, password);
                if ("teacher".equals(result)) {
                    createCookie(response, id);
                }
                break;
            case "4": // Admin authentication
                result = admin.authenticate(id, password);
                if ("admin".equals(result)) {
                    createCookie(response, id);
                }
                break;
            default: // Adding a teacher
                result = admin.addTeacher(id, password);
                break;
        }

        return result; // This should typically redirect to a meaningful URL or return a view name
    }

    private void createCookie(HttpServletResponse response, String username) {
        Cookie usernameCookie = new Cookie("username", username);
        usernameCookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(usernameCookie);
    }
}
