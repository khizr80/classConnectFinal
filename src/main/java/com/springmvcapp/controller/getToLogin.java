package com.springmvcapp.controller;

import com.springmvcapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class getToLogin {

    @Autowired
    private Admin a1;
    @Autowired
    private Teacher t1;
    @Autowired
    private Student s1;
    @GetMapping("/goto")
    public String goToTeacher(@RequestParam("id") String id, @RequestParam("pass") String pass,  @RequestParam("value") String value, Model model) {

        String y="";

        if(value.equals("1"))
        {
           y= s1.createUser(id,pass);
           return y;
        }

        else if (value.equals("2"))
        {
            y=  s1.authenticate(id,pass);
            return y;
        }

        else if(value.equals("3"))
        {
            y=  t1.authenticate(id,pass);
            return y;
        }

        else if(value.equals("4"))
        {
            y=  a1.authenticate(id,pass);
            return y;
        }

        else
        {
            y=a1.addTeacher(id,pass);
            return y;
        }

    }

}
