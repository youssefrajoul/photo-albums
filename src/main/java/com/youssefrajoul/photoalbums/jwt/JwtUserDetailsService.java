package com.youssefrajoul.photoalbums.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import com.youssefrajoul.photoalbums.business.UserService;

// @Service
// public class JwtUserDetailsService implements UserDetailsService {
// 	@Autowired
// 	UserService userService;

// 	@Override
// 	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// 		try {
// 			return (UserDetails) userService.getUser(username);
// 		} catch (Exception e) {
// 			throw new UsernameNotFoundException("User not found with username: " + username);
// 		}
// 	}
// }