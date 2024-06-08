package com.youssefrajoul.photoalbums.rest;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.youssefrajoul.photoalbums.business.AlbumService;
import com.youssefrajoul.photoalbums.business.PictureService;
import com.youssefrajoul.photoalbums.business.UserService;
import com.youssefrajoul.photoalbums.model.Picture;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
@AllArgsConstructor
@NoArgsConstructor
public class PictureRestController {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam String albumId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if (!albumService.getAlbum(Long.parseLong(albumId)).getUsername().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename())) {
                return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/upload").body("Invalid file");
            }
            byte[] imageBytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            pictureService.storeEncryptedPicture(base64Image, fileName, Long.parseLong(albumId), username);
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/pictures")
                    .body("Picture uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload picture: " + e.getMessage());
        }
    }

    @GetMapping("/picture")
    public ResponseEntity<?> fetchPicture(@RequestParam Long pictureId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            String username = authentication.getName();

            Picture picture = pictureService.retrieveEncryptedPicture(pictureId, username);
            if (picture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] imageBytes = Base64.getDecoder().decode(picture.getData());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust this based on your image type
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
