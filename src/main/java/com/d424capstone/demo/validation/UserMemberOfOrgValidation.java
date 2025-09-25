package com.d424capstone.demo.validation;

import com.d424capstone.demo.services.UserOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMemberOfOrgValidation implements AssignmentValidationRule{

    @Autowired
    private UserOrganizationService userOrganizationService;

    @Override
    public boolean validate(Integer userId, Integer eventId, Integer orgId) {
        return userOrganizationService.getUserOrganization(userId, orgId).isPresent();
    }

    @Override
    public String getErrorMessage(Integer userId, Integer eventId, Integer orgId) {
        return "User: " + userId + " is not a member of organization: " + orgId;
    }
}
