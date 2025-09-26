package com.d424capstone.demo.services;

import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

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


    
}

