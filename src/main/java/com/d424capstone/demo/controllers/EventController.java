package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.EventResponseDTO;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.services.BudgetService;
import com.d424capstone.demo.services.EventService;
import com.d424capstone.demo.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private BudgetService budgetService;

    @PostMapping("/api/organizations/{orgId}/events")
    public ResponseEntity<Event> createEvent(@PathVariable Integer orgId, @RequestBody Event newEvent) {
        if (organizationService.findById(orgId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization not found");
        }
        Event event = eventService.createEvent(newEvent, organizationService.findById(orgId).get());

        if (newEvent.getBudget() != null && newEvent.getBudget().compareTo(BigDecimal.ZERO) > 0) {
            try {
                budgetService.createBudget(event.getId(), newEvent.getBudget());
            } catch (Exception e) {
                System.err.println("Warning: Failed to create budget for event " + event.getId() + ": " + e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping("/api/organizations/{orgId}/events")
    public ResponseEntity<List<EventResponseDTO>> getEvents(@PathVariable Integer orgId,
                                                            @RequestParam(required = false) String type,
                                                            @RequestParam(required = false) String status,
                                                            @RequestParam(required = false) String startDate,
                                                            @RequestParam(required = false) String endDate) {
        try {
            List<EventResponseDTO> events = eventService.getEventsByOrgIdDTO(orgId);
            return ResponseEntity.ok(events);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/organizations/{orgId}/events/{eventId}")
    @Transactional(readOnly = true)
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Integer orgId, @PathVariable Integer eventId) {
        Event event = eventService.getEventByIdAndOrgId(eventId, orgId);


        EventResponseDTO eventDTO = new EventResponseDTO(
                event.getId(),
                event.getEventName(),
                event.getEventDescription(),
                event.getEventType(),
                event.getEventStatus(),
                event.getEventDate().toString(), 
                event.getOrg().getId()
        );

        return ResponseEntity.ok(eventDTO);
    }

    @PutMapping("/api/organizations/{orgId}/events/{eventId}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @RequestBody Event updatedEvent
    ) {
        updatedEvent.setId(eventId);
        updatedEvent.setOrg(organizationService.findById(orgId).get());

        return ResponseEntity.ok(eventService.updateEvent(updatedEvent, orgId));
    }

    @DeleteMapping("/api/organizations/{orgId}/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer orgId, @PathVariable Integer eventId) {
//        Event event = eventService.getEventByIdAndOrgId(eventId, orgId);
        eventService.deleteEvent(eventId, orgId);
        return ResponseEntity.noContent().build();
    }
}
