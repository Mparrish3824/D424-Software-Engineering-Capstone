package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Budget;
import com.d424capstone.demo.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findAllByEvent_Id(Integer eventId);
    List<Expense> findAllByCategory(String category);
    List<Expense> findAllByPaymentStatus(String paymentStatus);
    List<Expense> findAllByVendorName(String vendorName);

    List<Expense> findAllByEvent_IdAndCategory(Integer eventId, String category);
    List<Expense> findAllByEvent_IdAndPaymentStatus(Integer eventId, String paymentStatus);
    List<Expense> findAllByEvent_IdAndVendorName(Integer eventId, String vendorName);
    List<Expense> findAllByEvent_IdAndDateIncurredBetween(Integer eventId, LocalDate startDate, LocalDate endDate);

    List<Expense> findAllByAmountGreaterThan(BigDecimal amount);
    List<Expense> findAllByAmountLessThan(BigDecimal amount);
    List<Expense> findAllByEvent_IdAndAmountGreaterThan(Integer eventId, BigDecimal amount);


    List<Expense> findAllByEvent_IdAndCategoryAndPaymentStatus(Integer eventId, String category, String paymentStatus);
    List<Expense> findAllByEvent_IdAndCategoryAndVendorName(Integer eventId, String category, String vendorName);
    List<Expense> findAllByEvent_IdAndPaymentStatusAndVendorName(Integer eventId, String paymentStatus, String vendorName);
    List<Expense> findAllByEvent_IdAndCategoryAndPaymentStatusAndVendorName(Integer eventId, String category, String paymentStatus, String vendorName);


    @Query ("SELECT e FROM Expense e WHERE e.event.org.id = :orgId")
    List<Expense> findAllByOrgId(@Param ("orgId") Integer orgId);

    @Query("SELECT e FROM Expense e WHERE e.event.org.id = :orgId AND e.paymentStatus = :paymentStatus")
    List<Expense> findAllByOrgIdAndPaymentStatus(@Param("orgId") Integer orgId, @Param("paymentStatus") String paymentStatus);

    @Query("SELECT e FROM Expense e WHERE e.event.org.id = :orgId AND e.category = :category")
    List<Expense> findAllByOrgIdAndCategory(@Param("orgId") Integer orgId, @Param("category") String category);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.event.id = :eventId")
    BigDecimal findTotalExpensesByEventId(@Param("eventId") Integer eventId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.event.id = :eventId AND e.paymentStatus = 'paid'")
    BigDecimal findPaidExpensesByEventId(@Param("eventId") Integer eventId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.event.id = :eventId AND e.paymentStatus = 'pending'")
    BigDecimal findPendingExpensesByEventId(@Param("eventId") Integer eventId);

}