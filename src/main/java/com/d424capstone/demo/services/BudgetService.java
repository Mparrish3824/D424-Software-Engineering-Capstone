package com.d424capstone.demo.services;

import com.d424capstone.demo.entities.Budget;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ExpenseService expenseService;


// ============================================================================
//                         Validation Methods
// ============================================================================

    public Budget validateBudgetExists(Integer budgetId){
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        if (budget.isEmpty()){
            throw new RuntimeException("Budget not found with ID: " + budgetId);
        }
        return budget.get();
    }

    public Budget validateBudgetExistsByEventId(Integer eventId){
        Optional<Budget> budget = budgetRepository.findByEvent_Id(eventId);
        if (budget.isEmpty()){
            throw new RuntimeException("Budget not found with ID: " + eventId);
        }
        return budget.get();
    }

// ============================================================================
//                            CRUD Methods
// ============================================================================

    public Budget createBudget(Integer eventId, BigDecimal amountTotal){
        Event event = eventService.validateEventExists(eventId);
        Optional<Budget> existingBudget = budgetRepository.findByEvent_Id(eventId);
        if (existingBudget.isPresent()){
            throw new RuntimeException("Budget already exists for event ID: " + eventId);
        }

        Budget budget = new Budget();
        budget.setEvent(event);
        budget.setAmountTotal(amountTotal);
        budget.setAmountSpent(BigDecimal.ZERO);
        budget.setAmountRemaining(amountTotal);
        budget.setCreatedAt(Instant.now());
        budget.setUpdatedAt(Instant.now());

        return budgetRepository.save(budget);
    }

    public Budget updateBudgetTotal(Integer budgetId, BigDecimal newAmountTotal){
        Budget budget = validateBudgetExists(budgetId);

        budget.setAmountTotal(newAmountTotal);
        budget.setUpdatedAt(Instant.now());

        budget.setAmountRemaining(newAmountTotal.subtract(budget.getAmountSpent()));

        return budgetRepository.save(budget);
    }

    @Transactional
    public Budget recalculateBudgetFromExpenses(Integer budgetId){
        Budget budget = validateBudgetExists(budgetId);

        // get total from expenses
        BigDecimal totalSpent = expenseService.getTotalExpensesByEventId(budget.getEvent().getId());

        budget.setAmountSpent(totalSpent);
        budget.setAmountRemaining(budget.getAmountTotal().subtract(totalSpent));
        budget.setUpdatedAt(Instant.now());

        return budgetRepository.save(budget);
    }

    public void deleteBudget(Integer budgetId){
        Budget budget = validateBudgetExists(budgetId);
        budgetRepository.deleteById(budgetId);
    }

// ============================================================================
//                            Lookup Methods
// ============================================================================

    public Budget getBudgetById(Integer budgetId) {
        return validateBudgetExists(budgetId);
    }

    public Budget getBudgetByEventId(Integer eventId) {
        return validateBudgetExistsByEventId(eventId);
    }

    public List<Budget> getBudgetsByOrgId(Integer orgId) {
        return budgetRepository.findAllByOrgId(orgId);
    }

// ============================================================================
//                           Business Logic
// ============================================================================

    public BigDecimal calculateBudgetPercentageUsed(Integer budgetId) {
        Budget budget = validateBudgetExists(budgetId);

        if(budget.getAmountTotal().equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }

        return budget.getAmountSpent()
                .divide(budget.getAmountTotal(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(1, RoundingMode.HALF_UP);
    }

    public boolean isBudgetOverLimit(Integer budgetId) {
        Budget budget = validateBudgetExists(budgetId);
        return budget.getAmountSpent().compareTo(budget.getAmountTotal()) > 0;
    }

    public boolean isBudgetNearingLimit(Integer budgetId, BigDecimal thresholdPercentage){
        Budget budget = validateBudgetExists(budgetId);
        BigDecimal percentageUsed = calculateBudgetPercentageUsed(budgetId);
        return percentageUsed.compareTo(thresholdPercentage) >= 0;
    }

    public List<Budget> getOverBudgetEvents() {
        return budgetRepository.findOverBudgetEvents();
    }

    public List<Budget> getOverBudgetEventsByOrgId(Integer orgId) {
        return budgetRepository.findOverBudgetEventsByOrgId(orgId);
    }

    public List<Budget> getBudgetsNearingLimit(Integer orgId, BigDecimal thresholdAmount) {
        return budgetRepository.findBudgetsNearingLimitByOrgId(orgId, thresholdAmount);
    }

}
