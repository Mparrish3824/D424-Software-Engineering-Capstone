package com.d424capstone.demo.validation;

import com.d424capstone.demo.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrgExistsValidation implements AssignmentValidationRule {

    @Autowired
    private OrganizationService organizationService;

    @Override
    public boolean validate(Integer userId, Integer eventId, Integer orgId) {
        return organizationService.findById(orgId).isPresent();
    }

    @Override
    public String getErrorMessage(Integer userId, Integer eventId, Integer orgId) {
        return "Organization not found with id: " + orgId;
    }
}
