package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.SubtaskCreateRequest;
import com.d424capstone.demo.dto.SubtaskResponseDTO;
import com.d424capstone.demo.dto.SubtaskUpdateRequest;
import com.d424capstone.demo.entities.Subtask;
import com.d424capstone.demo.services.SubtaskService;
import com.d424capstone.demo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubtaskController {
    @Autowired
    private SubtaskService subtaskService;

    @Autowired
    private TaskService taskService;  // For validation

    @PostMapping("/api/tasks/{taskId}/subtasks")
    public ResponseEntity<SubtaskResponseDTO> createSubtask(
            @PathVariable Integer taskId,
            @RequestBody SubtaskCreateRequest request) {

        Subtask subtask = subtaskService.createSubtask(
                request.getSubtaskName(),
                request.getSubtaskStatus(),
                taskId,
                request.getDueDate(),
                request.getSubtaskDescription()
        );

        SubtaskResponseDTO responseDTO = subtaskService.getSubtaskResponseById(subtask.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping ("/api/subtasks/{subtaskId}")
    public ResponseEntity<SubtaskResponseDTO> updateSubtask(
            @PathVariable Integer subtaskId,
            @RequestBody SubtaskUpdateRequest request) {

        if (request.getSubtaskName() != null) {
            subtaskService.updateSubtaskName(subtaskId, request.getSubtaskName());
        }
        if (request.getSubtaskStatus() != null) {
            subtaskService.updateSubtaskStatus(subtaskId, request.getSubtaskStatus());
        }
        if (request.getSubtaskDescription() != null) {
            subtaskService.updateSubtaskDescription(subtaskId, request.getSubtaskDescription());
        }
        if (request.getDueDate() != null) {
            subtaskService.updateSubtaskDueDate(subtaskId, request.getDueDate());
        }

        SubtaskResponseDTO responseDTO = subtaskService.getSubtaskResponseById(subtaskId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/api/subtasks/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Integer subtaskId) {

        subtaskService.deleteSubtask(subtaskId);

        return ResponseEntity.noContent().build();
    }

}
