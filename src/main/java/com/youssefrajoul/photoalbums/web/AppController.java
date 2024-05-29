package com.youssefrajoul.photoalbums.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.youssefrajoul.photoalbums.business.Business;
import com.youssefrajoul.photoalbums.model.User;

import jakarta.validation.Valid;
import lombok.Data;



@Controller
@Data
public class AppController {

    @Autowired
    private Business business;

    public AppController() {

    }

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/private")
    public String privé() {
        return "private";
    }

    @GetMapping("/register")
    public String getMethodName(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/newUser")
    public String postMethodName(@ModelAttribute("student") @Valid User user, BindingResult bindingResult, Model model) {
        business.addUser(user);
        return "redirect:/login";
    }
    
    
}
