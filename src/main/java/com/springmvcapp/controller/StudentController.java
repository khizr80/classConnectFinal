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
        model.addAttribute("courseId", courseId);
        return "courseDetails"; // Redirect or forward to a view displaying details of the submitted course
    }
    @PostMapping("/viewStreamMessages")
    public String viewStreamMessages(@RequestParam("courseId") String courseId,Model model) {
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,courseId,"c");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // View name or redirection
    }

    @PostMapping("/sendStreamMessage")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        String g=s.getTeacherUsernameByCourseId(courseId);
        model.addAttribute("teacherUsername", g);
        return "message"; // Redirect after sending message
    }
    @PostMapping("/sendStreamMessage2")
    public String sendStreamMessage(@RequestParam("courseId") String courseId,
                                    @RequestParam("messageText") String messageText,
                                    Model model,@RequestParam("action") String action,
                                    @RequestParam("teacherUsername") String t) {

        if ("course".equals(action)) {
            return s.insertMessage(username,courseId,"c",messageText);
        }
        else
        {
           return s.insertMessage(username,t,"t",messageText);
        }
    }


    @PostMapping("/viewTeacherMessages")
    public String viewTeacherMessages(@RequestParam("courseId") String courseId,Model model) {
        System.out.println(courseId);
        String g=s.getTeacherUsernameByCourseId(courseId);
        System.out.println(g);
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,g,"t");
        model.addAttribute("messages", messages); // Add messages to the model
        if (messages != null) {
            for (Map<String, Object> message : messages) {
                System.out.println("Sender: " + message.get("sender"));
                System.out.println("Receiver: " + message.get("receiver"));
                System.out.println("Text: " + message.get("text"));
                System.out.println("Date: " + message.get("date"));
                System.out.println("Role: " + message.get("role"));
                System.out.println("------------------------");
            }
        } else {
            System.out.println("No messages found.");
        }
        return "streamMessages"; // View name or redirection
    }
}

