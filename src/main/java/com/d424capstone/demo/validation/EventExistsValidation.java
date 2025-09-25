package com.d424capstone.demo.validation;

import com.d424capstone.demo.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventExistsValidation implements AssignmentValidationRule{

    @Autowired
    private EventService eventService;

    @Override
    public boolean validate(Integer userId, Integer eventId, Integer orgId) {
        return eventService.findEventById(eventId).isPresent();
    }

    @Override
    public String getErrorMessage(Integer userId, Integer eventId, Integer orgId) {
        return "Event not found with id: " + eventId;
    }
}
