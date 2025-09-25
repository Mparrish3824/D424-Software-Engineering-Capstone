package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.BudgetResponseDTO;
import com.d424capstone.demo.entities.Budget;
import com.d424capstone.demo.services.BudgetService;
import com.d424capstone.demo.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @Autowired
    EventService eventService;

// ============================================================================
//                           POST MAPPING
// ============================================================================

    @PostMapping("/api/organizations/{orgId}/events/{eventId}/budget")
    @Transactional
    public ResponseEntity<Budget> createBudget(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @RequestParam BigDecimal amountTotal){
        try {
            eventService.validateEventBelongsToOrg(eventId, orgId);
            Budget createdBudget = budgetService.createBudget(eventId, amountTotal);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

// ============================================================================
//                            GET MAPPING
// ============================================================================

    @GetMapping("/api/organizations/{orgId}/events/{eventId}/budget")
    @Transactional
    public ResponseEntity<BudgetResponseDTO> getBudgetForEvent(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId){
        try{
            eventService.validateEventBelongsToOrg(eventId, orgId);
            Budget budget = budgetService.getBudgetByEventId(eventId);
            BudgetResponseDTO response = mapToBudgetResponseDTO(budget);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/api/organizations/{orgId}/budgets/over-budget")
    public ResponseEntity<List<BudgetResponseDTO>> getOverBudgetEvents(@PathVariable Integer orgId) {
        try {
            List<Budget> overBudgetEvents = budgetService.getOverBudgetEventsByOrgId(orgId);
            List<BudgetResponseDTO> responses = overBudgetEvents.stream()
                    .map(this::mapToBudgetResponseDTO)
                    .toList();

            return ResponseEntity.ok(responses);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/organizations/{orgId}/budgets")
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgetsForOrg(
            @PathVariable Integer orgId){
        try{
            List<Budget> budgets = budgetService.getBudgetsByOrgId(orgId);
            List<BudgetResponseDTO> responses = budgets.stream()
                    .map(this::mapToBudgetResponseDTO)
                    .toList();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

// ============================================================================
//                            PUT MAPPING
// ============================================================================

    @PutMapping("/api/organizations/{orgId}/events/{eventId}/budget")
    @Transactional
    public ResponseEntity<BudgetResponseDTO> updateBudgetTotal(@PathVariable Integer orgId,
                                                               @PathVariable Integer eventId,
                                                               @RequestParam BigDecimal amountTotal) {
        try {
            eventService.validateEventBelongsToOrg(eventId, orgId);

            Budget budget = budgetService.getBudgetByEventId(eventId);
            Budget updatedBudget = budgetService.updateBudgetTotal(budget.getId(), amountTotal);
            BudgetResponseDTO response = mapToBudgetResponseDTO(updatedBudget);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.out.println("Budget update error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/organizations/{orgId}/events/{eventId}/budget/recalculate")
    public ResponseEntity<BudgetResponseDTO> recalculateBudget(@PathVariable Integer orgId,
                                                               @PathVariable Integer eventId) {
        try {
            // Validate event belongs to organization
            eventService.validateEventBelongsToOrg(eventId, orgId);

            Budget budget = budgetService.getBudgetByEventId(eventId);
            Budget recalculatedBudget = budgetService.recalculateBudgetFromExpenses(budget.getId());
            BudgetResponseDTO response = mapToBudgetResponseDTO(recalculatedBudget);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


// ============================================================================
//                            DELETE MAPPING
// ============================================================================

    @DeleteMapping("/api/organizations/{orgId}/events/{eventId}/budget")
    public ResponseEntity<Void> deleteBudget(@PathVariable Integer orgId,
                                             @PathVariable Integer eventId) {
        try {
            // Validate event belongs to organization
            eventService.validateEventBelongsToOrg(eventId, orgId);

            Budget budget = budgetService.getBudgetByEventId(eventId);
            budgetService.deleteBudget(budget.getId());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

// ============================================================================
//                            DTO MAPPING
// ============================================================================

    private BudgetResponseDTO mapToBudgetResponseDTO(Budget budget) {
        BudgetResponseDTO dto = new BudgetResponseDTO();
        dto.setBudgetId(budget.getId());
        dto.setEventId(budget.getEvent().getId());
        dto.setEventName(budget.getEvent().getEventName());
        dto.setAmountTotal(budget.getAmountTotal());
        dto.setAmountSpent(budget.getAmountSpent());
        dto.setAmountRemaining(budget.getAmountRemaining());
        dto.setPercentageUsed(budgetService.calculateBudgetPercentageUsed(budget.getId()));
        dto.setIsOverBudget(budgetService.isBudgetOverLimit(budget.getId()));
        dto.setCreatedAt(budget.getCreatedAt());
        dto.setUpdatedAt(budget.getUpdatedAt());

        return dto;
    }

}
