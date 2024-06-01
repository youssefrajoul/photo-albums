package com.youssefrajoul.photoalbums.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.youssefrajoul.photoalbums.business.UserService;
import com.youssefrajoul.photoalbums.model.User;

import jakarta.validation.Valid;
import lombok.Data;

@Controller
@Data
public class AppController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/register-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("student") @Valid User user, BindingResult bindingResult, Model model) {
        try {
            userService.signUp(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/login";
    }
}
