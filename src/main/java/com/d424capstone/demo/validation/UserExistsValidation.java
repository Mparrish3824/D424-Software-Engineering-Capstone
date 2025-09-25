package com.d424capstone.demo.validation;

import com.d424capstone.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserExistsValidation implements AssignmentValidationRule{

    @Autowired
    private UserService userService;


    @Override
    public boolean validate(Integer userId, Integer eventId, Integer orgId) {
        return userService.findById(userId).isPresent();
    }

    @Override
    public String getErrorMessage(Integer userId, Integer eventId, Integer orgId) {
        return "User not found";
    }
}
