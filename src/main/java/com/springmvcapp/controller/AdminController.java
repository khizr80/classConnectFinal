package com.springmvcapp.controller;
import com.springmvcapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AdminController {

    @Autowired
    private Admin a1;
    @PostMapping("/addteacher")
    public String addTeacher(Model model) {
        System.out.println(56);
        model.addAttribute("value", 5);
        return "login"; // Ensure this redirects or forwards to the appropriate view
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
}
