package com.d424capstone.demo.controllers;

import com.d424capstone.demo.entities.Invitation;
import com.d424capstone.demo.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @PostMapping("/api/organizations/{orgId}/invitations")
    public Invitation createInvitation(
            @PathVariable Integer orgId,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam String senderEmail
    ) {
        String stringOrgId = orgId.toString();
        return invitationService.createInvitation(email, role, senderEmail, stringOrgId);
    }

    @GetMapping("/api/organizations/{orgId}/invitations")
    public List<Invitation> getInvitationsByOrg(
            @PathVariable Integer orgId,
            @RequestParam(required = false) String status
    ) {
        if (status == null) {
            return invitationService.getAllInvitationsByOrg(orgId);
        } else if (status.equals("pending")) {
            return invitationService.getPendingInvitationsByOrg(orgId);
        } else if (status.equals("expired")) {
            return invitationService.getExpiredInvitationsByOrg(orgId);
        } else {
            throw new RuntimeException("Invalid status parameter");
        }
    }

    @PutMapping("/api/organizations/{orgId}/invitations/accept")
    public Invitation acceptInvitation(
            @PathVariable Integer orgId,
            @RequestParam String token
    ) {
        return invitationService.acceptInvitation(token);
    }

    @DeleteMapping("/api/organizations/{orgId}/invitations")
    public ResponseEntity<String> cancelInvitation(
            @PathVariable Integer orgId,
            @RequestParam String token
    ) {
        invitationService.cancelInvitation(token);
        return ResponseEntity.ok("Invitation cancelled");
    }
}
