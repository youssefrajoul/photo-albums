package com.youssefrajoul.photoalbums.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.youssefrajoul.photoalbums.business.PictureService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
@AllArgsConstructor
@NoArgsConstructor
public class PictureController {

    @Autowired
    private PictureService pictureService;

    // Endpoint to upload a picture
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPicture(@RequestParam("file") MultipartFile file) {
        try {
            pictureService.storeEncryptedPicture(file.getBytes());
            return ResponseEntity.status(HttpStatus.OK).body("Picture uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload picture: " + e.getMessage());
        }
    }

    // Endpoint to download all decrypted pictures
    @GetMapping("/download")
    public ResponseEntity<List<String>> downloadPictures(@RequestBody SecretKey privateKey) {
        try {
            List<String> decryptedPictures = pictureService.getAllDecryptedPictures(privateKey);
            return ResponseEntity.ok(decryptedPictures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
