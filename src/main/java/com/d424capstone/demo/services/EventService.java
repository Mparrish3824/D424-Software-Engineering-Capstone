package com.d424capstone.demo.services;

import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.repositories.EventRepository;
import com.d424capstone.demo.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private OrganizationRepository organizationRepository;


    // validate event
    public Event validateEventExists(Integer eventId){
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isEmpty()){
            throw new RuntimeException("Event not found with id " + eventId);
        }
        return event.get();
    }

    public void validateEventBelongsToOrg(Integer eventId, Integer orgId){
        Event event = validateEventExists(eventId);
        if(!event.getOrg().getId().equals(orgId)){
            throw new RuntimeException("Event ID " + eventId + " does not belong to organization " + orgId);
        }
    }


    // Create new event
    public Event createEvent(Event event, Organization org) {
        event.setOrg(org);
        return eventRepository.save(event);
    }

    // Update Event
    public Event updateEvent(Event event, Integer orgId){
        return eventRepository.save(event);
    }

    // Look up methods
    public Optional<Event> findEventById(Integer eventId){
        return eventRepository.findById(eventId);
    }

    public List<Event> getEventsByOrgId(Integer orgId){
        return eventRepository.findAllByOrgId(orgId);
    }

    public List<Event> getUpcomingEvents(Integer orgId){
        return eventRepository.findByEventDateAfterAndOrgId(LocalDate.now(), orgId);

    }

    public Event getEventByIdAndOrgId(Integer eventId,  Integer orgId){
        return eventRepository.findByIdAndOrgId(eventId, orgId);
    }

    public List<Event> getEventsByTypeAndOrgId(String type, Integer orgId){
        return eventRepository.findByEventTypeAndOrgId(type, orgId);

    }

    public List<Event> getEventsByEventStatusAndOrgId(String status, Integer orgId){
        return eventRepository.findByEventStatusAndOrgId(status, orgId);
    }

    public List<Event> getEventsByDateBetweenAndOrgId(LocalDate startDate, LocalDate endDate, Integer orgId){
        return eventRepository.findByEventDateBetweenAndOrgId(startDate, endDate, orgId);

    }

    public List<Event> getEventByNameAndOrgId(String name, Integer orgId){
        return eventRepository.findByEventNameAndOrgId(name, orgId);
    }

    public List<Event> getEventsByTypeAndStatusAndDateBetweenAndOrgId(String type, String status, LocalDate startDate, LocalDate endDate, Integer orgId){
        return eventRepository.findByEventTypeAndEventStatusAndEventDateBetweenAndOrgId(type,status, startDate, endDate, orgId);
    }

    public List<Event> getEventsByTypeAndStatusAndOrgId(String type, String status, Integer orgId){
        return eventRepository.findByEventTypeAndEventStatusAndOrgId(type, status, orgId);
    }

    public List<Event> getEventsByTypeAndDateBetweenAndOrgId(String type, LocalDate startDate, LocalDate endDate, Integer orgId){
        return eventRepository.findByEventTypeAndEventDateBetweenAndOrgId(type, startDate, endDate, orgId);
    }

    public List<Event> getEventsByStatusAndDateBetweenAndOrgId(String status, LocalDate startDate, LocalDate endDate, Integer orgId){
        return eventRepository.findByEventStatusAndEventDateBetweenAndOrgId(status, startDate, endDate, orgId);
    }


    public List<Event> getFilteredEvents(
            Integer orgId,
            String type,
            String status,
            String startDateString,
            String endDateString){

        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            startDate = startDateString != null ? LocalDate.parse(startDateString) : null;
            endDate = endDateString != null ? LocalDate.parse(endDateString) : null;
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Please use YYYY-MM-DD format");
        }


        if(type != null && status != null && startDateString != null && endDateString != null){
            return getEventsByTypeAndStatusAndDateBetweenAndOrgId(type, status, startDate, endDate, orgId);
        } else if (type != null && status != null && startDateString == null && endDateString == null){
            return getEventsByTypeAndStatusAndOrgId(type, status, orgId);
        } else if (type !=null && status == null && startDateString != null && endDateString != null){
            return getEventsByTypeAndDateBetweenAndOrgId(type, startDate, endDate, orgId);
        } else if (type == null && status != null && startDateString != null && endDateString != null){
            return getEventsByStatusAndDateBetweenAndOrgId(status, startDate, endDate, orgId);
        } else if (type != null && status == null && startDateString == null && endDateString == null){
            return getEventsByTypeAndOrgId(type, orgId);
        } else if (type == null && status != null && startDateString == null && endDateString == null){
            return getEventsByEventStatusAndOrgId(status, orgId);
        } else if (type == null && status == null && startDateString != null && endDateString != null){
            return getEventsByDateBetweenAndOrgId(startDate, endDate, orgId);
        }  else {
            return getEventsByOrgId(orgId);
        }
    }

    public void deleteEvent(Integer orgId, Integer eventId) {
        Event event = eventRepository.findByIdAndOrgId(eventId, orgId);
        eventRepository.delete(event);
    }
}
