package com.d424capstone.demo.services;


import com.d424capstone.demo.dto.SubtaskResponseDTO;
import com.d424capstone.demo.dto.TaskResponseDTO;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.entities.Subtask;
import com.d424capstone.demo.entities.Task;
import com.d424capstone.demo.repositories.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EventService eventService;
    @Autowired
    SubtaskService subtaskService;


// ============================================================================
//                         VALIDATION METHODS
// ============================================================================
    private void validateEventExists(Integer eventId) {
        if (eventService.findEventById(eventId).isEmpty()) {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }
    private Task validateTaskExists(Integer taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        return taskOpt.get();
    }

// ============================================================================
//                         BASIC CRUD METHODS
// ============================================================================

    public Task createTask(Integer eventId, String taskName, String taskDescription, String taskStatus, String taskPriority, LocalDate dueDate) {

        // validate and get Event Object
        Optional<Event> eventOpt = eventService.findEventById(eventId);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
        Event event = eventOpt.get();

        // use constructor to create task
        Task task = new Task(
            null,
            event,
            taskName,
            taskDescription,
            taskStatus,
            taskPriority,
            dueDate,
            Instant.now(),
            Instant.now()
        );

        // save and return
        return taskRepository.save(task);
    }

    public Task updateTaskName(Integer taskId, String taskName) {
        Task task = validateTaskExists(taskId);
        task.setTaskName(taskName);
        return taskRepository.save(task);
    }

    public Task updateTaskDescription(Integer taskId, String taskDescription) {
        Task task = validateTaskExists(taskId);
        task.setTaskDescription(taskDescription);
        return taskRepository.save(task);
    }

    public Task updateTaskPriority(Integer taskId, String taskPriority) {
        Task task = validateTaskExists(taskId);
        task.setTaskPriority(taskPriority);
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Integer taskId, String taskStatus) {
        Task task = validateTaskExists(taskId);
        task.setTaskStatus(taskStatus);
        return taskRepository.save(task);
    }

    public Task updateTaskDueDate(Integer taskId, LocalDate dueDate) {
        Task task = validateTaskExists(taskId);
        task.setDueDate(dueDate);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasksByEventId(Integer eventId) {
        return taskRepository.findAllByEvent_Id(eventId);
    }

    public Task getTaskById(Integer taskId) {
        return validateTaskExists(taskId);
    }

    public void deleteTask(Integer taskId) {
        Task task = validateTaskExists(taskId);
        taskRepository.delete(task);
    }

// ============================================================================
//                         CALCULATION METHODS
// ============================================================================

    public Double calculateTaskCompletionPercentage(Integer taskId) {
        List<Subtask> subtasks = subtaskService.getAllSubtasksByTaskId(taskId);

        if (subtasks.isEmpty()) {
            return null; // in case of no subtasks - task completion will be null
        }

        // collect amount of subtasks marked with the enum "completed" status
        long completedCount = subtasks.stream()
                .filter(subtask -> "completed".equals(subtask.getSubtaskStatus()))
                .count();

        // calculate percentage
        return (double) completedCount / subtasks.size() * 100;
    }
// ============================================================================
//                                  DTO
// ============================================================================

    @Transactional(readOnly = true)
    protected TaskResponseDTO mapToTaskResponseDTO(Task task, Double completionPercentage, List<Subtask> subtasks) {
        List<SubtaskResponseDTO> subtaskDTOs = subtasks.stream()
                .map(this::mapToSubtaskResponseDTO)
                .collect(Collectors.toList());

        return new TaskResponseDTO(
                task.getId(),
                task.getEvent().getId(),
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                completionPercentage,
                subtaskDTOs
        );
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTasksWithDetailsById(Integer taskId){
        Task task = validateTaskExists(taskId);
        Double completionPercentage = calculateTaskCompletionPercentage(taskId);
        List<Subtask> subtasks = subtaskService.getAllSubtasksByTaskId(taskId);
        return mapToTaskResponseDTO(task, completionPercentage, subtasks);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksWithDetailsByEventId(Integer eventId) {
        List<Task> tasks = getAllTasksByEventId(eventId);

        return tasks.stream()
                .map(task -> {
                    Double completionPercentage = calculateTaskCompletionPercentage(task.getId());
                    List<Subtask> subtasks = subtaskService.getAllSubtasksByTaskId(task.getId());
                    return mapToTaskResponseDTO(task, completionPercentage, subtasks);
                })
                .collect(Collectors.toList());
    }

    private SubtaskResponseDTO mapToSubtaskResponseDTO(Subtask subtask) {
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




}
