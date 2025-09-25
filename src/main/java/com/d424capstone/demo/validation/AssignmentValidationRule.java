package com.d424capstone.demo.validation;


public interface AssignmentValidationRule {

    boolean validate(Integer userId, Integer eventId, Integer orgId);
    String getErrorMessage(Integer userId, Integer eventId, Integer orgId);

}
