package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.UserOrganizationResponseDTO;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import com.d424capstone.demo.services.UserOrganizationService;
import com.d424capstone.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrganizationService userOrganizationService;


    @GetMapping("/api/users/{userId}/organizations")
    public ResponseEntity<List<UserOrganizationResponseDTO>> getUserOrganizations(@PathVariable Integer userId) {
        try {
            // Validate user exists
            User user = userService.validateUserExists(userId);

            // Get organizations with DTO mapping
            List<UserOrganizationResponseDTO> organizations = userOrganizationService.getOrganizationsByUserId(userId);

            return ResponseEntity.ok(organizations);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
