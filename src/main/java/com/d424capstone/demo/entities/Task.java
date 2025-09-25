package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "tasks")
public class Task {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "task_id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "event_id")
    private Event event;

    @Column (name = "task_name", nullable = false, length = 250)
    private String taskName;

    @Lob
    @Column (name = "task_description")
    private String taskDescription;

    @ColumnDefault ("'not_started'")
    @Column (name = "task_status")
    private String taskStatus;

    @ColumnDefault ("'medium'")
    @Column (name = "task_priority")
    private String taskPriority;

    @Column (name = "due_date")
    private LocalDate dueDate;

    @Column (name = "created_at")
    private Instant createdAt;

    @Column (name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "parentTask", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subtask> subtasks = new ArrayList<>();


    public Task(Integer id, Event event, String taskName, String taskDescription, String taskStatus, String taskPriority, LocalDate dueDate, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.event = event;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Task() {

    }
}