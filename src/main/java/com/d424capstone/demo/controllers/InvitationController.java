package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.InvitationResponseDTO;
import com.d424capstone.demo.entities.Invitation;
import com.d424capstone.demo.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional (readOnly = true)
    public ResponseEntity<List<InvitationResponseDTO>> getInvitationsByOrg(
            @PathVariable Integer orgId,
            @RequestParam(required = false) String status) {

        List<Invitation> invitations;

        if (status == null) {
            invitations = invitationService.getAllInvitationsByOrg(orgId);
        } else if (status.equals("pending")) {
            invitations = invitationService.getPendingInvitationsByOrg(orgId);
        } else if (status.equals("expired")) {
            invitations = invitationService.getExpiredInvitationsByOrg(orgId);
        } else {
            throw new RuntimeException("Invalid status parameter");
        }

        List<InvitationResponseDTO> response = invitations.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    private InvitationResponseDTO mapToDTO(Invitation inv) {
        return new InvitationResponseDTO(
                inv.getId(),                    
                inv.getEmail(),
                inv.getOrgRole(),
                inv.getInvitedBy().getEmail(),
                inv.getInvitationStatus(),
                inv.getCreatedDate() != null ? inv.getCreatedDate().toString() : null,
                inv.getExpiresAt() != null ? inv.getExpiresAt().toString() : null,
                inv.getInvitationToken()
        );
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
