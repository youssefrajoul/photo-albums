package com.youssefrajoul.photoalbums.business;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youssefrajoul.photoalbums.model.Picture;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.AlbumRepository;
import com.youssefrajoul.photoalbums.repository.PictureRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;
import com.youssefrajoul.photoalbums.security.EncryptionUtil;
import com.youssefrajoul.photoalbums.security.PictureEncryptionUtil;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    public void storeEncryptedPicture(String picture, String fileName, Long albumId, String username) {
        try {
            User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));

            // Encrypt the picture
            // String encryptedPicture = PictureEncryptionUtil.encrypt(picture,
            // PictureEncryptionUtil.stringToKey(user.getPublicKey()));

            // Create a new Picture object
            Picture newPicture = new Picture();
            newPicture.setName(fileName);
            newPicture.setUsername(user);
            newPicture.setData(picture);
            newPicture.setAlbum(albumRepository.findById(albumId).get());
            // Save the new picture in the repository
            pictureRepository.save(newPicture);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    public List<String> getAllDecryptedPictures(String username, String password) throws Exception {
        List<String> decryptedPictures = new ArrayList<>();
        try {

            // Fetch user
            User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));

            // Decrypt user's private key
            String encryptedPrivateKey = user.getEncryptedPrivateKey();
            byte[] salt = user.getSalt().getBytes();
            String decryptedPrivateKeyString = EncryptionUtil.decryptPrivateKey(encryptedPrivateKey, password, salt);
            PrivateKey privateKey = EncryptionUtil.stringToPrivateKey(decryptedPrivateKeyString);

            // Fetch pictures for the user
            List<Picture> pictures = pictureRepository.findByUsername(user);

            // Decrypt pictures
            for (Picture picture : pictures) {
                String decryptedPicture = PictureEncryptionUtil.decrypt(picture.getEncryptedPicture().getBytes(),
                        EncryptionUtil.stringToPrivateKey(encryptedPrivateKey));
                decryptedPictures.add(decryptedPicture);
            }

            return decryptedPictures;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedPictures;
    }

    public List<Picture> retrieveAllPictures(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
        return pictureRepository.findByUsername(user);
    }

    public Picture retrieveEncryptedPicture(Long pictureId, String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
        // return pictureRepository.findById(pictureId).get();
        try {
            if (pictureRepository.findByUsername(user)
                    .contains(pictureRepository.findById(pictureId).get())) {
                return pictureRepository.findById(pictureId).get();
            } else {
                throw new RuntimeException("Not allowed To get other users pictures");
            }

        } catch (Exception e) {
            return null;
        }
    }

}