package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.AssignmentRequestDTO;
import com.d424capstone.demo.dto.AssignmentResponseDTO;
import com.d424capstone.demo.entities.UserEventAssignment;
import com.d424capstone.demo.services.UserEventAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
public class UserEventAssignmentController {

    @Autowired
    private UserEventAssignmentService userEventAssignmentService;


// ============================================================================
//                             POST MAPPING
// ============================================================================

    @PostMapping("/api/organizations/{orgId}/events/{eventId}/assignments")
    public ResponseEntity<String> createAssignment(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @RequestBody AssignmentRequestDTO request) {

        UserEventAssignment assignment = userEventAssignmentService.createAssignment(
                eventId, orgId, request.getUserId(),
                request.getStartTime(), request.getEndTime(),
                request.getRoleDescription(), request.getNotes()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Assignment created successfully with ID: " + assignment.getId());
    }

// ============================================================================
//                              GET MAPPING
// ============================================================================

    @GetMapping ("/api/organizations/{orgId}/events/{eventId}/assignments")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByEvent(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId) {

        List<AssignmentResponseDTO> assignments = userEventAssignmentService.getAssignmentsByEventIdAsDTO(eventId, orgId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/api/assignments/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId) {

        AssignmentResponseDTO assignment = userEventAssignmentService.getAssignmentByIdAsDTO(assignmentId, orgId);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/api/assignments/user/{userId}") //All assignments for a user, cross org scope
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByUserId(
            @PathVariable Integer userId) {
        List<AssignmentResponseDTO> assignments = userEventAssignmentService.getAssignmentsByUserIdAsDTO(userId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/api/users/{userId}/assignments")  // User assignments within specific org
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByUserAndOrg(
            @PathVariable Integer userId,
            @RequestParam Integer orgId) {
        List<AssignmentResponseDTO> assignments = userEventAssignmentService.getAssignmentsByUserAndOrgAsDTO(userId, orgId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/api/organizations/{orgId}/events/{eventId}/assignments/status/{status}")  // Filter by status
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByStatus(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @PathVariable String status) {
        List<AssignmentResponseDTO> assignments = userEventAssignmentService.getAssignmentsByStatusAsDTO(eventId, orgId, status);
        return ResponseEntity.ok(assignments);
    }

// ============================================================================
//                               PUT MAPPING
// ============================================================================

    @PutMapping("/api/assignments/{assignmentId}/status")
    public ResponseEntity<String> updateAssignmentStatus(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId,
            @RequestParam String status){
        userEventAssignmentService.updateAssignmentStatus(assignmentId, orgId, status);
        return ResponseEntity.ok("Assignment status updated successfully");
    }

    @PutMapping("/api/assignments/{assignmentId}/shift")
    public ResponseEntity<String> updateAssignmentShift(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime){
        userEventAssignmentService.updateAssignmentShift(assignmentId, orgId, startTime, endTime);
        return ResponseEntity.ok("Assignment shift updated successfully");
    }

    @PutMapping("/api/assignments/{assignmentId}/role")
    public ResponseEntity<String> updateAssignmentRole(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId,
            @RequestParam String role){
        userEventAssignmentService.updateAssignmentRole(assignmentId, orgId, role);
        return ResponseEntity.ok("Assignment role updated successfully");
    }

    @PutMapping("/api/assignments/{assignmentId}/notes")
    public ResponseEntity<String> updateAssignmentNotes(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId,
            @RequestParam String notes){
        userEventAssignmentService.updateAssignmentNotes(assignmentId, orgId, notes);
        return ResponseEntity.ok("Assignment notes updated successfully");
    }

// ============================================================================
//                             DELETE MAPPING
// ============================================================================

    @DeleteMapping("/api/assignments/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam Integer orgId){
        userEventAssignmentService.deleteAssignment(assignmentId, orgId);
        return ResponseEntity.ok("Assignment deleted successfully");
    }


}
