package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subtasks")
public class Subtask {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "subtask_id", nullable = false)
    private Integer id;

    @Column (name = "subtask_name", nullable = false, length = 250)
    private String subtaskName;

    @ColumnDefault ("'not_started'")
    @Column (name = "subtask_status")
    private String subtaskStatus;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "parent_task_id", nullable = false)
    private Task parentTask;

    @Column (name = "due_date")
    private LocalDate dueDate;

    @Lob
    @Column (name = "subtask_description")
    private String subtaskDescription;

    @Column (name = "created_at")
    private Instant createdAt;

    @Column (name = "updated_at")
    private Instant updatedAt;


    public Subtask(Integer id, String subtaskName, String subtaskStatus, Task parentTask, LocalDate dueDate, String subtaskDescription, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.subtaskName = subtaskName;
        this.subtaskStatus = subtaskStatus;
        this.parentTask = parentTask;
        this.dueDate = dueDate;
        this.subtaskDescription = subtaskDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Subtask() {

    }
}
