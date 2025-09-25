package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.TaskCreateRequest;
import com.d424capstone.demo.dto.TaskResponseDTO;
import com.d424capstone.demo.dto.TaskUpdateRequest;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.entities.Task;
import com.d424capstone.demo.services.EventService;
import com.d424capstone.demo.services.SubtaskService;
import com.d424capstone.demo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    SubtaskService subtaskService;
    @Autowired
    EventService eventService;



// ============================================================================
//                          VALIDATION METHOD
// ============================================================================

    private void validateEventBelongsToOrg(Integer eventId, Integer orgId) {
        Event event = eventService.findEventById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (!event.getOrg().getId().equals(orgId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event does not belong to this organization");
        }
    }

// ============================================================================
//                             POST MAPPING
// ============================================================================

    @PostMapping ("/api/organizations/{orgId}/events/{eventId}/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @RequestBody TaskCreateRequest request) {

        // Validate event belongs to org
        validateEventBelongsToOrg(eventId, orgId);

        // Create task
        Task task = taskService.createTask(
                eventId,
                request.getTaskName(),
                request.getTaskDescription(),
                request.getTaskStatus(),
                request.getTaskPriority(),
                request.getDueDate()
        );

        TaskResponseDTO responseDTO = taskService.getTasksWithDetailsById(task.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
// ============================================================================
//                             GET MAPPING
// ============================================================================

    @GetMapping("/api/organizations/{orgId}/events/{eventId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Integer orgId, @PathVariable Integer eventId, @PathVariable Integer taskId) {
        validateEventBelongsToOrg(eventId, orgId);
        TaskResponseDTO responseDTO = taskService.getTasksWithDetailsById(taskId);
        if(!responseDTO.getEventId().equals(eventId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task does not belong to this Event");
        }
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/api/organizations/{orgId}/events/{eventId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByEventId(@PathVariable Integer orgId, @PathVariable Integer eventId) {
        validateEventBelongsToOrg(eventId, orgId);
        List<TaskResponseDTO> responseDTOS = taskService.getTasksWithDetailsByEventId(eventId);
        return ResponseEntity.ok(responseDTOS);
    }

// ============================================================================
//                             PUT MAPPING
// ============================================================================

    @PutMapping("/api/organizations/{orgId}/events/{eventId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @PathVariable Integer taskId,
            @RequestBody TaskUpdateRequest request) {
        validateEventBelongsToOrg(eventId, orgId);

        TaskResponseDTO existingTask = taskService.getTasksWithDetailsById(taskId);
        if (!existingTask.getEventId().equals(eventId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task does not belong to this event");
        }
        if (request.getTaskName() != null) {
            taskService.updateTaskName(taskId, request.getTaskName());
        }
        if (request.getTaskDescription() != null) {
            taskService.updateTaskDescription(taskId, request.getTaskDescription());
        }
        if (request.getTaskStatus() != null) {
            taskService.updateTaskStatus(taskId, request.getTaskStatus());
        }
        if (request.getTaskPriority() != null) {
            taskService.updateTaskPriority(taskId, request.getTaskPriority());
        }
        if (request.getDueDate() != null) {
            taskService.updateTaskDueDate(taskId, request.getDueDate());
        }

        return ResponseEntity.ok(taskService.getTasksWithDetailsById(taskId));
    }


// ============================================================================
//                           DELETE MAPPING
// ============================================================================

    @DeleteMapping("/api/organizations/{orgId}/events/{eventId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @PathVariable Integer taskId) {

        validateEventBelongsToOrg(eventId, orgId);

        TaskResponseDTO existingTask = taskService.getTasksWithDetailsById(taskId);
        if (!existingTask.getEventId().equals(eventId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task does not belong to this event");
        }

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }}
