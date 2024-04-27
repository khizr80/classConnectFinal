package com.springmvcapp.controller;
import com.springmvcapp.model.Course;  // Assuming your Course class is in the model package

import com.springmvcapp.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
@Controller
public class TeacherController {
    private final Teacher s;


    public TeacherController(Teacher s) {
        this.s = s;
    }
    String username;
    @PostMapping("/viewCourseTeacher")
    public String viewCourse(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        List<Course> courses = s.getCoursesByTeacherUsername(username);
        model.addAttribute("courses", courses);
        System.out.println(courses.get(0));
        return "courseTeacher"; // Ensure this redirects or forwards to the appropriate view
    }

}
