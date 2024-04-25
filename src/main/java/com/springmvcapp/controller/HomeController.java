package com.springmvcapp.controller;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/redirect") // Changed from @PostMapping to @GetMapping
    public String redirectToNewPage(@RequestParam(required = false)String value,Model model) {
            model.addAttribute("value", value);
            return "login" ;
    }
}
