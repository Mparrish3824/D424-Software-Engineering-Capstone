package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Integer> {

    List<Subtask> findAllByParentTask_Id(Integer parentTaskId);

}
