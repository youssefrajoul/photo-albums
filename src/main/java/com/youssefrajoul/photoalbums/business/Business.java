package com.youssefrajoul.photoalbums.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youssefrajoul.photoalbums.model.Authority;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.AuthorityRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;

@Service
public class Business {
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public void addUser(User user) {
        String password = "{noop}" + user.getPassword();
        User newUser = new User(user.getUsername(), password, true);
        Authority newAuthority = new Authority(1, newUser.getUsername(), "prof");
        usersRepository.save(newUser);
        authorityRepository.save(newAuthority);
    }
}
