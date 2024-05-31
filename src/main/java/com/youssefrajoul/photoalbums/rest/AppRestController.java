package com.youssefrajoul.photoalbums.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youssefrajoul.photoalbums.business.Business;
import com.youssefrajoul.photoalbums.model.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
@AllArgsConstructor
@NoArgsConstructor
public class AppRestController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Business service;

    public AppRestController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody User user) {
        service.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @PostMapping(value = "/login")
    // public ResponseEntity<String> login(@Valid @RequestBody User user) {
    // authenticationManager
    // .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
    // user.getPassword()));
    // System.out.println("test test test test :" + user.getPassword());
    // String token = JwtHelper.generateToken(user.getUsername());
    // return ResponseEntity.ok(token);
    // }

    // spring boot doc example @PostMapping("/login")
    // public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
    //     return ResponseEntity.status(HttpStatus.CREATED).build();
    // }

    @GetMapping("/test")
    public String getMethodName() {
        return new String("hello mfs");
    }

}
