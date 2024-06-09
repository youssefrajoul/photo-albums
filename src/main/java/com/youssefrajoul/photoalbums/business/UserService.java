package com.youssefrajoul.photoalbums.business;

import java.security.KeyPair;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youssefrajoul.photoalbums.model.Authority;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.AuthorityRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;
import com.youssefrajoul.photoalbums.security.EncryptionUtil;
import com.youssefrajoul.photoalbums.security.KeyPairUtil;

@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(User user) throws Exception {
        // Check if the user already exists
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new Exception("User with username " + user.getUsername() + " already exists.");
        }

        // Generate a salt and encrypt the private key with the user's password
        try {
            byte[] salt = EncryptionUtil.generateSalt();
            // Store the public key, encrypted private key, and salt in the database
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password with BCrypt
            newUser.setPublicKey(user.getPublicKey());
            newUser.setEnabled(true);
            newUser.setSalt(Base64.getEncoder().encodeToString(salt));

            userRepository.save(newUser);

            Authority authority = new Authority();
            authority.setUsername(newUser.getUsername());
            authority.setAuthority("user");
            authorityRepository.save(authority);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Failed to sign up user: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("An unexpected error occurred: " + e.getMessage());
        }
    }

    public User getUser(String username){
        return userRepository.findById(username).get();
    }

}