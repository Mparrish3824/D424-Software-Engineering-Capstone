package com.d424capstone.demo.services;

import com.d424capstone.demo.dto.SubtaskResponseDTO;
import com.d424capstone.demo.entities.Subtask;
import com.d424capstone.demo.entities.Task;
import com.d424capstone.demo.repositories.SubtaskRepository;
import com.d424capstone.demo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubtaskService {

    @Autowired
    SubtaskRepository subtaskRepository;
    @Autowired
    TaskRepository taskRepository;


// ============================================================================
//                         VALIDATION METHODS
// ============================================================================

    private Subtask validateSubtaskExists(Integer subtaskId){
        Optional<Subtask> subtaskOpt = subtaskRepository.findById(subtaskId);
        if(subtaskOpt.isEmpty()){
            throw new RuntimeException("Subtask with id " + subtaskId + " does not exist");
        }
        return subtaskOpt.get();
    }

// ============================================================================
//                         BASIC CRUD METHODS
// ============================================================================

    public Subtask createSubtask(String subtaskName, String subtaskStatus, Integer parentTaskId, LocalDate dueDate, String subtaskDescription){

        Optional<Task> taskOpt = taskRepository.findById(parentTaskId);
        if (taskOpt.isEmpty()) {
            throw new RuntimeException("Task not found with ID: " + parentTaskId);
        }
        Task task = taskOpt.get();


        // use constructor
        Subtask subtask = new Subtask(
                null,
                subtaskName,
                subtaskStatus,
                task,
                dueDate,
                subtaskDescription,
                Instant.now(),
                Instant.now()
        );

        return subtaskRepository.save(subtask);
    }

    public Subtask updateSubtaskName(Integer subtaskId, String subtaskName){
        Subtask subtask = validateSubtaskExists(subtaskId);
        subtask.setSubtaskName(subtaskName);
        return subtaskRepository.save(subtask);
    }

    public Subtask updateSubtaskStatus(Integer subtaskId, String subtaskStatus){
        Subtask subtask = validateSubtaskExists(subtaskId);
        subtask.setSubtaskStatus(subtaskStatus);
        return subtaskRepository.save(subtask);
    }

    public Subtask updateSubtaskDescription(Integer subtaskId, String subtaskDescription){
        Subtask subtask = validateSubtaskExists(subtaskId);
        subtask.setSubtaskDescription(subtaskDescription);
        return subtaskRepository.save(subtask);
    }

    public Subtask updateSubtaskDueDate(Integer subtaskId, LocalDate dueDate){
        Subtask subtask = validateSubtaskExists(subtaskId);
        subtask.setDueDate(dueDate);
        return subtaskRepository.save(subtask);
    }

    public void deleteSubtask(Integer subtaskId){
        Subtask subtask = validateSubtaskExists(subtaskId);
        subtaskRepository.delete(subtask);
    }

// ============================================================================
//                       All Subtasks for a Task
// ============================================================================

    public List<Subtask> getAllSubtasksByTaskId(Integer taskId){
        return subtaskRepository.findAllByParentTask_Id(taskId);
    }


// ============================================================================
//                            DTO
// ============================================================================

    @Transactional(readOnly = true)
    protected SubtaskResponseDTO mapToSubtaskResponseDTO(Subtask subtask){
        return new SubtaskResponseDTO(
                subtask.getId(),
                subtask.getSubtaskName(),
                subtask.getSubtaskStatus(),
                subtask.getSubtaskDescription(),
                subtask.getDueDate(),
                subtask.getCreatedAt(),
                subtask.getUpdatedAt()
        );

    }

    @Transactional(readOnly = true)
    public SubtaskResponseDTO getSubtaskResponseById(Integer subtaskId) {
        Subtask subtask = validateSubtaskExists(subtaskId);
        return mapToSubtaskResponseDTO(subtask);
    }

}
