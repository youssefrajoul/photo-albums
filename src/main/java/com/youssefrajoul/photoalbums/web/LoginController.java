package com.youssefrajoul.photoalbums.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Data;

@Controller
@Data
public class LoginController {

    public LoginController() {

    }

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
}
