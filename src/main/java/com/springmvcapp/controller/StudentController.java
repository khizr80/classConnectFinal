package com.springmvcapp.controller;
import com.springmvcapp.model.Course;  // Assuming your Course class is in the model package

import com.springmvcapp.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String viewCourse(Model model, HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        List<Course> courses = s.getCoursesByUsername(username);
        model.addAttribute("courses", courses);
        return "course"; // Ensure this redirects or forwards to the appropriate view
    }

    @PostMapping("/submitCourse")
    public String handleCourseSubmission(@RequestParam("courseId") String courseId,Model model) {
        model.addAttribute("courseId", courseId);
        return "courseDetails"; // Redirect or forward to a view displaying details of the submitted course
    }
    @PostMapping("/viewStreamMessages")
    public String viewStreamMessages(@RequestParam("courseId") String courseId,Model model) {
        List<Map<String, Object>> messages=s.getStreamMessages(username,courseId,"c");
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
        String g=s.getTeacherUsernameByCourseId(courseId);
        List<Map<String, Object>> messages=s.getMessagesBySenderReceiverAndRole(username,g,"t");
        model.addAttribute("messages", messages); // Add messages to the model
        return "streamMessages"; // View name or redirection
    }

    @PostMapping("/viewTranscript")
    public String viewTranscript(Model model, HttpServletRequest request) {
        String username = getUsernameFromCookie(request);
        List<Map<String, Object>> transcript = s.getTranscriptByUsername(username);
        model.addAttribute("transcript", transcript);
        return "transcript";
    }

    private String getUsernameFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @PostMapping("/viewMarksStudent")
    public String viewMarksStudent(@RequestParam("courseId") String courseId,Model model) {
        String f=s.getTeacherUsernameByCourseId(courseId);
        List<Map<String, Object>> marksData= s.getMarks(username,f,courseId);
        model.addAttribute("marksData", marksData);
        return "viewMarksStudentPage"; // Return the name of the Thymeleaf template to render
    }
    @PostMapping("/registerCoursesStudent")
    public String registerCourses(Model model) {
        List<Map<String, Object>> activeCourses = s.getAllActiveCourses();
        model.addAttribute("activeCourses", activeCourses);
        return "activeCourses"; // Redirect to the page to view courses after registration
    }
    @PostMapping("/registerCoursesStudent2")
    public String registerCoursesStudent2(@RequestParam("courseInfo") String courseInfo, HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        String[] parts = courseInfo.split(":");
        String courseId = parts[0];
        String teacherUsername = parts[1];
        String courseName = parts[2];
        return s.registerCourse(courseId,teacherUsername,courseName,username);

    }
    @PostMapping("/viewStudentAttendance")
    public String viewStudentAttendance(@RequestParam("courseId") String courseId, Model model) {
        model.addAttribute("courseId", courseId);
        String g=s.getTeacherUsernameByCourseId(courseId);
        List<Map<String, Object>> attendanceData= s.getAttendanceByStudentAndCourseAndTeacher(username,courseId,g);
        model.addAttribute("attendanceData", attendanceData);
        return "studentAttendanceView";
    }
}

