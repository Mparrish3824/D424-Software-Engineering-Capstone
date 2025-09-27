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
            System.out.println("=== getUserOrganizations called with userId: " + userId + " ===");

            // Validate user exists
            System.out.println("Step 1: Validating user exists...");
            User user = userService.validateUserExists(userId);
            System.out.println("Step 1 SUCCESS: User found - " + user.getUsername());

            // Get organizations with DTO mapping
            System.out.println("Step 2: Getting organizations by userId...");
            List<UserOrganizationResponseDTO> organizations = userOrganizationService.getOrganizationsByUserId(userId);
            System.out.println("Step 2 SUCCESS: Found " + organizations.size() + " organizations");

            // Log each organization
            for (int i = 0; i < organizations.size(); i++) {
                UserOrganizationResponseDTO org = organizations.get(i);
                System.out.println("Org " + i + ": " + org.getOrgName() + " (ID: " + org.getOrgId() + ")");
            }

            System.out.println("Step 3: Returning ResponseEntity.ok()...");
            return ResponseEntity.ok(organizations);

        } catch (Exception e) {
            System.out.println("ERROR in getUserOrganizations: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
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
