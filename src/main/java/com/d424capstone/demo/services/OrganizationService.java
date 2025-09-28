package com.d424capstone.demo.services;

import com.d424capstone.demo.dto.OrganizationStatsDTO;
import com.d424capstone.demo.entities.*;
import com.d424capstone.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    private String generateNewCode() {
        // Generate Code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        while(code.length() < 6 ) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString();
    }

    // Create New Organization
    public Organization createNewOrganization(Organization organization) {
        int maxRetries = 5;
        int attempts = 0;
        // Set Code
        organization.setOrgCode(generateNewCode());
        // Check if code exists
        while(organizationRepository.findByOrgCode(organization.getOrgCode()).isPresent()) {
            attempts++;
            if(attempts > maxRetries) {
                throw new RuntimeException("Unable to generate unique organization code after" + maxRetries + " attempts");
            }
            organization.setOrgCode(generateNewCode());
        }
        return organizationRepository.save(organization);
    }

    // Update Organization
    public Organization updateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    // Look up Methods for OrgName, OrgId and OrgCode
    public List<Organization> findAllByOrgName(String orgName) {
        List<Organization> orgs = organizationRepository.findAllByOrgName(orgName);
        if (orgs.isEmpty()) {
            throw new RuntimeException("No organizations found with name: " + orgName);
        }
        return orgs;
    }

    public Optional<Organization> findById(Integer id) {
        return organizationRepository.findById(id);
    }

    public Organization findByOrgCode(String orgCode) {
        Optional<Organization> org = organizationRepository.findByOrgCode(orgCode);
        if (org.isEmpty()) {
            throw new RuntimeException("Organization not found with code: " + orgCode);
        }
        return org.get();
    }

    public void deleteOrganization(Integer orgId) {
        // Validate organization exists
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        organizationRepository.delete(org);
    }

    // Stats
    public OrganizationStatsDTO getOrganizationStats(Integer orgId) {
        // Validate
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // members and coordinators
        List<UserOrganization> userOrgs = userOrganizationRepository.findAllByOrgId(orgId);
        Integer totalMembers = userOrgs.size();
        Integer coordinators = (int) userOrgs.stream()
                .filter(uo -> "coordinator".equals(uo.getOrgRole()))
                .count();

        // event statistics
        List<Event> events = eventRepository.findAllByOrgId(orgId);
        Integer totalEvents = events.size();
        Integer activeEvents = (int) events.stream()
                .filter(e -> "active".equals(e.getEventStatus()))
                .count();

        // pending invitations
        List<Invitation> pendingInvitations = invitationRepository.findAllByInvitationPendingAndOrgId(orgId);
        Integer pendingInvitationsCount = pendingInvitations.size();

        // budget
        Double totalBudget = events.stream()
                .mapToDouble(event -> {
                    try {
                        Budget budget = budgetRepository.findByEvent_Id(event.getId()).orElse(null);
                        return budget != null ? budget.getAmountTotal().doubleValue() : 0.0;
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .sum();

        return new OrganizationStatsDTO(totalMembers, coordinators, totalEvents,
                activeEvents, pendingInvitationsCount, totalBudget);
    }

}
