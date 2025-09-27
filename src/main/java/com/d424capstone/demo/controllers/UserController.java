package com.d424capstone.demo.controllers;

import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import com.d424capstone.demo.services.UserOrganizationService;
import com.d424capstone.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrganizationService userOrganizationService;

    @GetMapping("/api/users/{userId}/organizations")
    @Transactional(readOnly = true)
    public List<UserOrganization> getOrganizationsByUser(@PathVariable Integer userId) {
        return userOrganizationService.getOrganizationsByUser(userId);
    }

    @PostMapping("/api/users")
    public ResponseEntity<User> createNewUser (@RequestBody User user) {
        User createdUser = userService.createNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<User> login (@RequestParam String username, @RequestParam String password) {
        try {
            User user = userService.authenticateUser(username, password);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/api/user/search")
    public User findByEmailOrUsername(@RequestParam(required = false) String email,
                                      @RequestParam(required = false) String username) {
        if (email != null) {
            return userService.findByEmail(email);
        } else if (username != null) {
            return userService.findByUsername(username);
        } else {
            throw new RuntimeException("Either email or username must be provided");
        }
    }



    
}




