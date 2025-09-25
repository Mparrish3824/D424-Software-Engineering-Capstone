package com.d424capstone.demo.services;

import com.d424capstone.demo.dto.AssignmentResponseDTO;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserEventAssignment;
import com.d424capstone.demo.repositories.UserEventAssignmentRepository;
import com.d424capstone.demo.validation.AssignmentValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEventAssignmentService {

    @Autowired
    private UserEventAssignmentRepository userEventAssignmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private List<AssignmentValidationRule> validationRules;


// ============================================================================
// VALIDATION METHODS - NO LONGER NEEDED, REPLACED WITH POLYMORPHIC VALIDATION
// ============================================================================

//    // Does the user exist
//    private boolean validateUserExists(Integer userId){
//        return userService.findById(userId).isPresent();
//    }
//
//    // Does the event exist
//    private boolean validateEventExists(Integer eventId){
//        return eventService.findEventById(eventId).isPresent();
//    }
//
//    // Does the organization exist
//    private boolean validateOrgExists(Integer orgId){
//        return organizationService.findById(orgId).isPresent();
//    }
//
//    // Does the event belong to the organization
//    private boolean validateEventBelongsToOrg(Integer eventId, Integer orgId){
//        return eventService.getEventByIdAndOrgId(eventId, orgId) != null;
//    }
//
//    // Is the user a member of the organization
//    private boolean validateUserIsMemberOfOrg(Integer userId, Integer orgId){
//        return userOrganizationService.getUserOrganization(userId, orgId).isPresent();
//    }




// ============================================================================
//                         BASIC CRUD METHODS
// ============================================================================

    public Optional<UserEventAssignment> findById(Integer assignmentId){
        return userEventAssignmentRepository.findById(assignmentId);
    }

    public List<UserEventAssignment> getAssignmentsByEventId(Integer eventId, Integer orgId){
        return userEventAssignmentRepository.findByEventIdAndOrgId(eventId, orgId);
    }

    // Cross org from user view
    public List<UserEventAssignment> getAssignmentsByUserId(Integer userId){
        return userEventAssignmentRepository.findByUserId(userId);
    }

    public List<UserEventAssignment> getAssignmentsByUserAndOrg(Integer userId, Integer orgId){
        return userEventAssignmentRepository.findByUserIdAndOrgId(userId, orgId);
    }

    public List<UserEventAssignment> getAssignmentsByStatus(Integer eventId, Integer orgId, String status){
        return userEventAssignmentRepository.findByEventIdAndOrgIdAndAssignmentStatus(eventId, orgId,  status);
    }

    public UserEventAssignment createAssignment(Integer eventId, Integer orgId, Integer userId, LocalTime startTime, LocalTime endTime, String roleDescription, String notes){
        // Polymorphic Validation
        for (AssignmentValidationRule rule : validationRules) {
            if (!rule.validate(userId, eventId, orgId)) {
                throw new RuntimeException(rule.getErrorMessage(userId, eventId, orgId));
            }
        }

        // get objects by id
        Event event = eventService.findEventById(eventId).get();
        User user = userService.findById(userId).get();
        Organization org = organizationService.findById(orgId).get();

        // use constructor to create assignment
        UserEventAssignment assignment = new UserEventAssignment(
                null,
                event,
                org,
                user,
                startTime,
                endTime,
                roleDescription,
                "pending",
                notes,
                Instant.now()
        );

        // save and return
        return userEventAssignmentRepository.save(assignment);
    }

    public void updateAssignmentStatus(Integer assignmentId, Integer orgId, String newStatus) {
        Optional<UserEventAssignment> assignmentOpt = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found or does not belong to organization");
        }

        UserEventAssignment assignment = assignmentOpt.get();
        assignment.setAssignmentStatus(newStatus);
        userEventAssignmentRepository.save(assignment);
    }

    public void updateAssignmentShift(Integer assignmentId, Integer orgId, LocalTime startTime, LocalTime endTime) {
        Optional<UserEventAssignment> assignmentOpt = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found or does not belong to organization");
        }

        UserEventAssignment assignment = assignmentOpt.get();
        assignment.setShiftStartTime(startTime);
        assignment.setShiftEndTime(endTime);
        userEventAssignmentRepository.save(assignment);
    }

    public void updateAssignmentRole(Integer assignmentId, Integer orgId, String roleDescription) {
        Optional<UserEventAssignment> assignmentOpt = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found or does not belong to organization");
        }

        UserEventAssignment assignment = assignmentOpt.get();
        assignment.setRoleDescription(roleDescription);
        userEventAssignmentRepository.save(assignment);
    }

    public void updateAssignmentNotes(Integer assignmentId, Integer orgId, String notes) {
        Optional<UserEventAssignment> assignmentOpt = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found or does not belong to organization");
        }

        UserEventAssignment assignment = assignmentOpt.get();
        assignment.setNotes(notes);
        userEventAssignmentRepository.save(assignment);
    }

    public void deleteAssignment(Integer assignmentId, Integer orgId) {
        try{
            UserEventAssignment assignment = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId).get();
            userEventAssignmentRepository.delete(assignment);
        } catch (RuntimeException e) {
            throw new RuntimeException("Assignment not found or does not belong to organization");
        }
    }

// ============================================================================
//                         MAPPING TO DTO
// ============================================================================

    @Transactional (readOnly = true)
    protected AssignmentResponseDTO mapToDTO(UserEventAssignment assignment) {
        return new AssignmentResponseDTO(
                assignment.getId(),
                assignment.getUser().getId(),
                assignment.getUser().getUsername(),
                assignment.getEvent().getId(),
                assignment.getEvent().getEventName(),
                assignment.getShiftStartTime(),
                assignment.getShiftEndTime(),
                assignment.getRoleDescription(),
                assignment.getAssignmentStatus(),
                assignment.getNotes(),
                assignment.getCreatedAt()
        );
    }

    @Transactional (readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByEventIdAsDTO(Integer eventId, Integer orgId) {
        List<UserEventAssignment> assignments = getAssignmentsByEventId(eventId, orgId);
        return assignments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional (readOnly = true)
    public AssignmentResponseDTO getAssignmentByIdAsDTO(Integer assignmentId, Integer orgId) {
        UserEventAssignment assignment = userEventAssignmentRepository.findByIdAndOrgId(assignmentId, orgId)
                .orElseThrow(() -> new RuntimeException("Assignment not found or does not belong to organization"));
        return mapToDTO(assignment);
    }

    @Transactional (readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByUserIdAsDTO(Integer userId){
        List<UserEventAssignment> assignments = getAssignmentsByUserId(userId);
        return assignments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional (readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByUserAndOrgAsDTO(Integer userId, Integer orgId) {
        List<UserEventAssignment> assignments = getAssignmentsByUserAndOrg(userId, orgId);
        return assignments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional (readOnly = true)
    public List<AssignmentResponseDTO> getAssignmentsByStatusAsDTO(Integer eventId, Integer orgId, String status) {
        List<UserEventAssignment> assignments = getAssignmentsByStatus(eventId, orgId, status);
        return assignments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
