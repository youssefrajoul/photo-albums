package com.youssefrajoul.photoalbums.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youssefrajoul.photoalbums.model.Authority;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.AuthorityRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class Business {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(User user) {
        Optional<User> existingUser = userRepository.findById(user.getUsername());
        if (existingUser.isPresent()) {
            throw new DuplicateKeyException(String.format("User with the username address '%s' already exists.", user.getUsername()));
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userRepository.save(new User(user.getUsername(), hashedPassword, true));
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority("user");
        authorityRepository.save(authority);
    }
}