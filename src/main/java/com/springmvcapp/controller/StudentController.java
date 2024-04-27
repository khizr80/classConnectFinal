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

import com.springmvcapp.model.*;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
        import org.springframework.ui.Model;

import java.util.List;

@Controller
    public class StudentController {
    private final Student s;

    public StudentController(Student s) {
        this.s = s;
    }

    @PostMapping("/viewCourse")
    public String viewCourse(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();

                    break;
                }
            }
        }
        System.out.println(username);
        List<Course> courses = s.getCoursesByUsername(username);
        model.addAttribute("courses", courses);
        System.out.println(courses.get(0));
        return "course"; // Ensure this redirects or forwards to the appropriate view
    }

    @PostMapping("/submitCourse")
    public String handleCourseSubmission(@RequestParam("courseId") String courseId) {
        // Handle the course ID, e.g., register the user for the course, display course details, etc.
        System.out.println("Course ID submitted: " + courseId);

        // Redirect to another page or return a view name
        return "courseDetails"; // Redirect or forward to a view displaying details of the submitted course
    }
}

