package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByEvent_Id(Integer eventId);

    List<Task> findAllByDueDateAndEvent_Id(LocalDate dueDate, Integer eventId);

    List<Task> findAllByTaskPriorityAndEvent_Id(String taskPriority, Integer eventId);

    List<Task> findAllByTaskStatusAndEvent_Id(String taskStatus, Integer eventId);

}
