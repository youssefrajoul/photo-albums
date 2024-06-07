package com.youssefrajoul.photoalbums.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.youssefrajoul.photoalbums.business.AlbumService;
import com.youssefrajoul.photoalbums.business.PictureService;
import com.youssefrajoul.photoalbums.business.UserService;
import com.youssefrajoul.photoalbums.model.Picture;
import com.youssefrajoul.photoalbums.model.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private PictureService pictureService;

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/private")
    public String getPrivate() {
        return "private";
    }

    @GetMapping("/register-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register-form";
    }

    // @PostMapping("/register")
    // public ResponseEntity<?> registerUser(@RequestBody User user, BindingResult bindingResult) {
    //     System.out.println("test test register");
    //     if (bindingResult.hasErrors()) {
    //         return ResponseEntity.badRequest().body("Invalid user data");
    //     }
    //     try {
    //         userService.signUp(user);
    //         return ResponseEntity.ok("User registered successfully");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user: " + e.getMessage());
    //     }
    // }

    @GetMapping("/upload-picture")
    public String getUploadForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("albums", albumService.getAlbums(username));
        return "upload-picture";
    }

    @GetMapping("/pictures")
    public String viewPictures(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Picture> pictures = pictureService.retrieveAllPictures(username);
        model.addAttribute("pictures", pictures);
        return "pictures";
    }

    @GetMapping("/albums")
    public String getMethodName(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("albums", albumService.getAlbums(username));
        return "albums";
    }

    
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }

}
