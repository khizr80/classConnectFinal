package com.springmvcapp.controller;
import com.springmvcapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private Admin a1;
    @PostMapping("/addteacher")
    public String addTeacher(Model model) {
        model.addAttribute("value", 5);
        model.addAttribute("headerText", "Add Teacher");
        model.addAttribute("buttonName", "Register");
        return "login"; // Ensure this redirects or forwards to the appropriate view
    }
   
    @PostMapping("/offerCourseForm")
    public String showOfferCourseForm() {
        return "offer_course_form"; // This will be a new HTML file for the form
    }

    @PostMapping("/offerCourse")
    public String offerCourse(@RequestParam("semester") int semester,
                              @RequestParam("courseName") String courseName,
                              Model model) {
        a1.offerCourse( semester, courseName);
        return "success";
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
    public String processApproval(@RequestParam String action,@RequestParam String username,Model model) {
        a1.updateStatusByUsername(username,action);
        return "redirect:/approveRegistration";
    }
    @GetMapping("/getRemoveTeacher")
    public String removeTeacher(Model model) {
        List<String>x=a1.getAllTeacherUsernames();
        model.addAttribute("usernames", x);
        model.addAttribute("userType", "teacher");
        return "deleteUser";
    }

    @GetMapping("/getRemoveStudent")
    public String removeStudent(Model model) {
        List<String>x=a1.getAllStudentUsernames();
        model.addAttribute("usernames", x);
        model.addAttribute("userType", "student");
        return "deleteUser";
    }
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("username") String username,@RequestParam("userType") String userType) {
         a1.deleteUser(username, userType);
                if (userType.equals("teacher")) {
                    return "redirect:/getRemoveTeacher"; // Redirect to admin dashboard or another appropriate page
                } 
                return "redirect:/getRemoveStudent"; // Redirect to admin dashboard or another appropriate page
    }
}
