package com.youssefrajoul.photoalbums.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youssefrajoul.photoalbums.business.UserService;
import com.youssefrajoul.photoalbums.jwt.JwtRequest;
import com.youssefrajoul.photoalbums.jwt.JwtResponse;
import com.youssefrajoul.photoalbums.jwt.JwtTokenUtil;
import com.youssefrajoul.photoalbums.model.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
@NoArgsConstructor
public class AuthRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup-request")
    public ResponseEntity<?> signUpUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        try {
            userService.signUp(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user: " + e.getMessage());
        }
    }

    @PostMapping("/login-request")
    public ResponseEntity<?> logInUser(@RequestBody JwtRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                            authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to log in user: " + e.getMessage());
        }
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

}