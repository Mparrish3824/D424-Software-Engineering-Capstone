package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.UserEventAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEventAssignmentRepository extends JpaRepository<UserEventAssignment, Integer> {

    // find everyone assigned to the event
    List<UserEventAssignment> findByEventIdAndOrgId(Integer eventId, Integer orgId);

    // find everyone filtered by assignment status
    List<UserEventAssignment> findByEventIdAndOrgIdAndAssignmentStatus(Integer eventId, Integer orgId, String status);

    // find all assignments for a specific user - full scope since the user may volunteer for multiple organizations
    List<UserEventAssignment> findByUserId(Integer userId);

    // find all assignments for a specific user for a specific organization
    List<UserEventAssignment> findByUserIdAndOrgId(Integer userId, Integer orgId);

    // find specific Assignment with org scope
    Optional<UserEventAssignment> findByIdAndOrgId(Integer assignmentId, Integer orgId);

}
