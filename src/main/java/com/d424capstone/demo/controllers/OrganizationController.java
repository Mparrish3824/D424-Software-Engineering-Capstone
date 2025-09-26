package com.d424capstone.demo.controllers;


import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController()
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    //Look up methods
    @GetMapping("/api/organizations/search/name")
    public List<Organization> findAll(@RequestParam String orgName) {
        return organizationService.findAllByOrgName(orgName);
    }

    @GetMapping("/api/organizations/search/code")
    public Organization findByCode(@RequestParam String code) {
        return organizationService.findByOrgCode(code);
    }
    @GetMapping("/api/organizations/{id}")
    public Organization findById(@PathVariable Integer id) {
        return organizationService.findById(id).get();
    }

    // Create new organization
    @PostMapping("/api/organizations")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        if(organization.getOrgName() == null || organization.getOrgName().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Organization createdOrg = organizationService.createNewOrganization(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrg);
    }

    // Update organization
    @PutMapping("/api/organizations/update")
    public Organization updateOrganization(@RequestBody Organization organization) {
        return organizationService.updateOrganization(organization);
    }
}

