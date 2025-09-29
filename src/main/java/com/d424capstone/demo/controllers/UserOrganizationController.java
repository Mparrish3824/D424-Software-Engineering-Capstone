package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.UserOrganizationResponseDTO;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import com.d424capstone.demo.services.UserOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @GetMapping("/api/organizations/{orgId}/users")
    @Transactional (readOnly = true)
    public ResponseEntity<List<UserOrganizationResponseDTO>> getUsersByOrganization(@PathVariable Integer orgId) {
        try {
            List<UserOrganization> userOrgs = userOrganizationService.getUsersByOrganization(orgId);

            List<UserOrganizationResponseDTO> response = userOrgs.stream()
                    .map(this::mapToDTO)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private UserOrganizationResponseDTO mapToDTO(UserOrganization userOrg) {
        return new UserOrganizationResponseDTO(
                userOrg.getId(),
                userOrg.getUser().getId(),
                userOrg.getUser().getUsername(),
                userOrg.getUser().getEmail(),
                userOrg.getUser().getFirstName(),
                userOrg.getUser().getLastName(),
                userOrg.getOrg().getId(),
                userOrg.getOrg().getOrgName(),
                userOrg.getOrg().getOrgCode(),
                userOrg.getOrgRole(),
                userOrg.getJoinedAt() != null ? userOrg.getJoinedAt().toString() : null
        );
    }

        // Get users by role
    @GetMapping("/api/organizations/{orgId}/users/{role}")
    public List<UserOrganization> getUsersByRoleInOrganization(@PathVariable Integer orgId, @PathVariable String role) {
        return userOrganizationService.getUsersByRoleInOrganization(orgId, role);
    }

    // PUT
    @PutMapping("/api/organizations/{orgId}/users/{username}")
    public ResponseEntity<UserOrganizationResponseDTO> updateUserToOrganization(
            @PathVariable Integer orgId,
            @PathVariable String username,
            @RequestBody String role) {
        try {
            UserOrganization updated = userOrganizationService.updateUserRole(username, orgId, role);
            UserOrganizationResponseDTO dto = mapToDTO(updated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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




