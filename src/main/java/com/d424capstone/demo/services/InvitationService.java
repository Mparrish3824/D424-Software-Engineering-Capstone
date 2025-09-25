package com.d424capstone.demo.services;

import com.d424capstone.demo.entities.Invitation;
import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.repositories.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserOrganizationService userOrganizationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;


    public Invitation createInvitation(String email, String role, String senderEmail, String orgId){
        // validate no pending invitation already exists
        Invitation existingInvitation = invitationRepository.findByEmail(email);
        Invitation invitation;
        if (existingInvitation == null){
            invitation = new Invitation();
        } else if (existingInvitation.getExpiresAt().isBefore(Instant.now())){
            invitationRepository.delete(existingInvitation);
            invitation = new Invitation();
        } else {
            throw new RuntimeException("Pending invitation already exists");
        }
        invitation.setEmail(email);
        invitation.setOrgRole(role);
        // validate organization exists
        Integer intOrgId = Integer.parseInt(orgId);
        Organization organization = organizationService.findById(intOrgId).get();
        if (organization == null){
            throw new RuntimeException("Organization not found");
        } else {
            invitation.setOrg(organization);
        }
        // set sender's email
        User invitingUser = userService.findByEmail(senderEmail);
        if (invitingUser == null){
            throw new RuntimeException("Inviting user not found");
        } else {
            invitation.setInvitedBy(invitingUser);
        }
        // generate UUID token
        invitation.setInvitationToken(UUID.randomUUID().toString());
        // set expiration timeout to 14 days
        invitation.setExpiresAt(Instant.now().plus(14, ChronoUnit.DAYS));
        // set status
        invitation.setInvitationStatus("pending");
        // return/save
        System.out.println("Invitation created! Token: " + invitation.getInvitationToken());
        System.out.println("Accept URL: http://localhost:8080/accept-invitation?token=" + invitation.getInvitationToken());
        return invitationRepository.save(invitation);
    }
    public Invitation acceptInvitation(String token){
        // find invitation by token
        Optional<Invitation> optionalInvitation = invitationRepository.findByInvitationToken(token);
        if (optionalInvitation.isEmpty()){
            throw new RuntimeException("Invalid invitation token");
        }
        Invitation invitation = optionalInvitation.get();
        // validate token is still pending
        if (invitation.getExpiresAt().isBefore(Instant.now())){
            throw new RuntimeException("Invitation expired");
        } else if (!invitation.getInvitationStatus().equals("pending")){
            throw new RuntimeException("Invitation already processed");
        }
        // create a new UserOrganization relationship
        User invUser = userService.findByEmail(invitation.getEmail());
        if (invUser == null){
            throw new RuntimeException("User Account not found. Please create an account first");
        }
        Organization invOrg = organizationService.findById(invitation.getOrg().getId()).get();
        userOrganizationService.addUserToOrganization(
                invUser.getUsername(),
                invOrg.getId(),
                invitation.getOrgRole()
        );
        // update the invitation status to accepted
        invitation.setInvitationStatus("accepted");
        // return success
        return invitationRepository.save(invitation);
    }

    public void cancelInvitation(String token) {
        Optional<Invitation> optionalInvitation = invitationRepository.findByInvitationToken(token);
        if (optionalInvitation.isEmpty()) {
            throw new RuntimeException("Invitation not found");
        }

        Invitation invitation = optionalInvitation.get();
        if (!invitation.getInvitationStatus().equals("pending")) {
            throw new RuntimeException("Can only cancel pending invitations");
        }

        invitationRepository.delete(invitation);
    }

    // look up methods
    public List<Invitation> getPendingInvitationsByOrg(Integer orgId){
        return invitationRepository.findAllByInvitationPendingAndOrgId(orgId);
    }
    public List<Invitation> getExpiredInvitationsByOrg(Integer orgId){
        return invitationRepository.findAllByInvitationExpiredAndOrgId(orgId);
    }
    public List<Invitation> getAllInvitationsByOrg(Integer orgId){
        return invitationRepository.findAllByOrgId(orgId);
    }
}
