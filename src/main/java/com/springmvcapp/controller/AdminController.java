package com.springmvcapp.controller;
import com.springmvcapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private Admin a1;
    @PostMapping("/addteacher")
    public String addTeacher(Model model) {
        System.out.println(56);
        model.addAttribute("value", 5);
        return "login1"; // Ensure this redirects or forwards to the appropriate view
    }
    @PostMapping("/remove")
    public String getRemove() {
        return "remove"; // Ensure this redirects or forwards to the appropriate view
    }

    @PostMapping("/removeteacher")
    public String remTeacher(@RequestParam("id") String id, @RequestParam("action") String action,Model model) {
        if ("removeStudent".equals(action)) {
            String y=a1.deleteStudent(id);
            return y;
            // Handle remove student action
        } else {
            // Handle remove teacher action
            String y=a1.deleteTeacher(id);
            return y; // Ensure this redirects or forwards to the appropriate view
        }
    }
    @PostMapping("/offerCourseForm")
    public String showOfferCourseForm() {
        return "offer_course_form"; // This will be a new HTML file for the form
    }

    @PostMapping("/offerCourse")
    public String offerCourse(@RequestParam("courseId") int courseId,
                              @RequestParam("semester") int semester,
                              @RequestParam("teacherUsername") String teacherUsername,
                              @RequestParam("courseName") String courseName,
                              Model model) {
        String result = a1.offerCourse(courseId, semester, teacherUsername, courseName);
        model.addAttribute("result", result);
        return "offer_course_result"; // This will be a new HTML file for showing the result
    }
    @GetMapping("/openRegistration")
    public String openRegistration(Model model) {
        List<Map<String, Object>> courses = a1.getAllCourses();


        model.addAttribute("courses", courses);
        return "courseRegistration"; // Return the view name for the success page
    }
    @PostMapping("/toggleRegistration")
    public String toggleRegistration(@RequestParam("courseId") String courseId,
                                     @RequestParam("teacherUsername") String teacherUsername,
                                     @RequestParam("status") String status) {
        System.out.println(courseId);
        System.out.println(teacherUsername);
        System.out.println("status "+status);
        a1.updateCourseStatus(courseId,teacherUsername,status);

        return "redirect:/openRegistration";

    }
    @GetMapping("/approveRegistration")
    public String approveRegistration(Model model) {
       List<String> x=a1.getUnprocessedStudentUsernames();
        model.addAttribute("unapprovedStudentUsernames", x);

        return "approveStudents"; // Assuming you have a success page named "success.html"
    }
    @PostMapping("/processApproval")
    public String processApproval(@RequestParam String action,
                                  @RequestParam String username,
                                  Model model) {
        System.out.println(action);
        System.out.println(username);
        a1.updateStatusByUsername(username,action);
        return "redirect:/approveRegistration";
    }
}
