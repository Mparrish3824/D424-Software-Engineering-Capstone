package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    Optional<Budget> findByEvent_Id(Integer eventId);

    List<Budget> findAllByAmountTotalGreaterThan(BigDecimal amount);
    List<Budget> findAllByAmountTotalLessThan(BigDecimal amount);
    List<Budget> findAllByAmountSpentGreaterThan(BigDecimal amount);
    List<Budget> findAllByAmountRemainingLessThan(BigDecimal threshold);

    @Query("SELECT b FROM Budget b WHERE b.event.org.id = :orgId")
    List<Budget> findAllByOrgId(@Param("orgId") Integer orgId);

    @Query("SELECT b FROM Budget b WHERE b.event.org.id = :orgId AND b.amountRemaining < :threshold")
    List<Budget> findBudgetsNearingLimitByOrgId(@Param("orgId") Integer orgId, @Param("threshold") BigDecimal threshold);

    @Query("SELECT b FROM Budget b WHERE b.amountSpent > b.amountTotal")
    List<Budget> findOverBudgetEvents();

    @Query("SELECT b FROM Budget b WHERE b.event.org.id = :orgId AND b.amountSpent > b.amountTotal")
    List<Budget> findOverBudgetEventsByOrgId(@Param("orgId") Integer orgId);
}

