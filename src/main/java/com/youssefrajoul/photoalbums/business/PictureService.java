package com.youssefrajoul.photoalbums.business;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youssefrajoul.photoalbums.model.Picture;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.PictureRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;
import com.youssefrajoul.photoalbums.security.EncryptionUtil;
import com.youssefrajoul.photoalbums.security.PictureEncryptionUtil;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    public void storeEncryptedPicture(byte[] picture) {
        User user = userRepository.findById("test").get();
        try {
            String encryptedPicture = PictureEncryptionUtil.encrypt(picture,
                    PictureEncryptionUtil.stringToKey(user.getPublicKey()));
            System.out.println("test test test" + encryptedPicture);
            Picture newPicture = new Picture();
            newPicture.setEncryptedPicture(encryptedPicture);
            newPicture.setUserId("test");
            pictureRepository.save(newPicture);
        } catch (Exception e) {
            // Handle exception
        }
    }

    public List<String> getAllDecryptedPictures(SecretKey privateKey) throws Exception {
        Iterable<Picture> pictures = pictureRepository.findAll();
        List<String> decryptedPictures = new ArrayList<>();
        for (Picture picture : pictures) {
            String decryptedPicture = PictureEncryptionUtil.decrypt(picture.getEncryptedPicture(), privateKey);
            decryptedPictures.add(decryptedPicture);
        }
        return decryptedPictures;
    }
}