package com.youssefrajoul.photoalbums.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youssefrajoul.photoalbums.business.AlbumService;
import com.youssefrajoul.photoalbums.business.PictureService;
import com.youssefrajoul.photoalbums.business.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/album")
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRestController {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/create-album")
    public ResponseEntity<?> createAlbum(@RequestParam String albumName) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            String username = authentication.getName();
            albumService.createAlbum(albumName, username);
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/albums")
                    .body(albumName + " created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create the album: " + e.getMessage());
        }
    }

    // @PostMapping("/share")
    // public String sharePicture(@RequestBody String pictureId) {

    // return entity;
    // }

    @PostMapping("/delete")
    public String deletePicture(@RequestParam String pictureId) {
        pictureService.deletePicture(Long.parseLong(pictureId));
        return "redirect:/pictures";
    }
}

