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

@Controller
    public class StudentController
{
    private final Student s;

    public StudentController(Student s) {
        this.s = s;
    }
    String username = null;

    @PostMapping("/viewCourse")
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
        System.out.println(username);
        List<Course> courses = s.getCoursesByUsername(username);
        model.addAttribute("courses", courses);
        System.out.println(courses.get(0));
        return "course"; // Ensure this redirects or forwards to the appropriate view
    }

    @PostMapping("/submitCourse")
    public String handleCourseSubmission(@RequestParam("courseId") String courseId,Model model) {
        // Handle the course ID, e.g., register the user for the course, display course details, etc.
        System.out.println("Course ID submitted: " + courseId);
        model.addAttribute("courseId", courseId);
        // Redirect to another page or return a view name
        return "courseDetails"; // Redirect or forward to a view displaying details of the submitted course
    }
    @PostMapping("/viewStreamMessages")
    public String viewStreamMessages(@RequestParam("courseId") String courseId,Model model) {
        System.out.println(courseId);
        // Logic to handle viewing stream messages
        return "streamMessages"; // View name or redirection
    }

    @PostMapping("/sendStreamMessage")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,Model model) {
        System.out.println(courseId);
        model.addAttribute("courseId", courseId);
        String g=s.getTeacherUsernameByCourseId(courseId);
        model.addAttribute("teacherUsername", g);
        System.out.println(g);
        return "message"; // Redirect after sending message
    }
    @PostMapping("/sendStreamMessage2")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,
                                    @RequestParam("messageText") String messageText,
                                    Model model,@RequestParam("action") String action,
                                    @RequestParam("teacherUsername") String t) {

        if ("course".equals(action)) {
            String v=s.insertMessage(username,courseId,"c",messageText);
        }
        else
        {
            System.out.println("come");
            System.out.println("teacher username "+t);
           String v=s.insertMessage(username,t,"t",messageText);
            System.out.println("go");

        }
        // Logic to process the message here

        return "message"; // Redirect to a view that confirms message sending or displays the message
    }


    @PostMapping("/viewTeacherMessages")
    public String viewTeacherMessages(@RequestParam("courseId") String courseId,Model model) {
        System.out.println(courseId);
        // Logic to handle viewing teacher messages
        return "teacherMessages"; // View name or redirection
    }
}

