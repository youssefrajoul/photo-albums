package com.youssefrajoul.photoalbums.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.youssefrajoul.photoalbums.business.AlbumService;
import com.youssefrajoul.photoalbums.business.PictureService;
import com.youssefrajoul.photoalbums.business.UserService;
import com.youssefrajoul.photoalbums.model.Picture;
import com.youssefrajoul.photoalbums.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @GetMapping("/hello")
	public String firstPage() {
		return new String("Hello World");
	}

    @GetMapping("/private")
    public String getPrivate() {
        return "private";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login-form";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup-form";
    }

    @GetMapping("/upload-picture")
    public String getUploadForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("albums", albumService.getAlbums(username));
        return "upload-picture";
    }

    @GetMapping("/pictures")
    public String viewPictures(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Redirect to login page if user is not authenticated
            return "redirect:/login";
        }
        // Check if authentication is via Basic Authentication
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            // Handle other types of authentication (e.g., JWT)
            return "redirect:/login";
        }
        // Authentication is via Basic Authentication
        String username = authentication.getName();
        List<Picture> pictures = pictureService.retrieveAllPictures(username);
        model.addAttribute("pictures", pictures);

        // Return the HTML file with pictures
        return "pictures"; // Assuming pictures.html exists in your templates directory
    }

    @GetMapping("/albums")
    public String getMethodName(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("albums", albumService.getAlbums(username));
        return "albums";
    }

}
