package com.d424capstone.demo.controllers;

import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import com.d424capstone.demo.services.UserOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserOrganizationController {
    @Autowired
    private UserOrganizationService userOrganizationService;

    // POST - Add User to organization
    @PostMapping("/api/organizations/{orgId}/users")
    public ResponseEntity<String> addUserToOrganization(
            @PathVariable Integer orgId,
            @RequestParam String username,
            @RequestParam String role) {
        try {
            UserOrganization userOrg = userOrganizationService.addUserToOrganization(username, orgId, role);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully added to the organization");
        } catch (RuntimeException e) {
            if(e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else if (e.getMessage().contains("already joined")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }
    // GET
        // Get all users
    @GetMapping("/api/organizations/{orgId}/users")
    public List<UserOrganization> getUsersByOrganization(@PathVariable Integer orgId) {
        return userOrganizationService.getUsersByOrganization(orgId);
    }

        // Get users by role
    @GetMapping("/api/organizations/{orgId}/users/{role}")
    public List<UserOrganization> getUsersByRoleInOrganization(@PathVariable Integer orgId, @PathVariable String role) {
        return userOrganizationService.getUsersByRoleInOrganization(orgId, role);
    }

    // PUT
        // Update user's role
    @PutMapping("/api/organizations/{orgId}/users/{username}")
    public ResponseEntity<String> updateUserToOrganization(
            @PathVariable Integer orgId,
            @PathVariable String username,
            @RequestBody String role) {
        try {
            userOrganizationService.updateUserRole(username, orgId, role);
            return ResponseEntity.status(HttpStatus.OK).body("User role successfully updated");
        } catch (RuntimeException e) {
            if(e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }  else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    // DELETE
        // Remove user from organization
    @DeleteMapping("/api/organizations/{orgId}/users/{username}")
    public ResponseEntity<String> deleteUserFromOrganization(
            @PathVariable Integer orgId,
            @PathVariable String username) {
        try {
            userOrganizationService.removeUserFromOrganization(username, orgId);
            return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted from the organization");
        } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}




