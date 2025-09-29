package com.d424capstone.demo.controllers;

import com.d424capstone.demo.dto.ExpenseRequestDTO;
import com.d424capstone.demo.dto.ExpenseResponseDTO;
import com.d424capstone.demo.dto.ExpenseUpdateRequestDTO;
import com.d424capstone.demo.entities.Expense;
import com.d424capstone.demo.services.BudgetService;
import com.d424capstone.demo.services.EventService;
import com.d424capstone.demo.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;
    @Autowired
    EventService eventService;
    @Autowired
    BudgetService budgetService;

// ============================================================================
//                           POST MAPPING
// ============================================================================

@PostMapping("/api/organizations/{orgId}/events/{eventId}/expenses")
public ResponseEntity<ExpenseResponseDTO> createExpense(
        @PathVariable Integer orgId,
        @PathVariable Integer eventId,
        @RequestBody ExpenseRequestDTO request){
    try{
        System.out.println("Creating expense for event: " + eventId);
        System.out.println("Request data: " + request.getExpenseDescription() + ", " + request.getAmount());
        
        eventService.validateEventBelongsToOrg(eventId, orgId);
        System.out.println("Event validation passed");
        
        Expense expense = expenseService.createExpense(
                eventId,
                request.getTitle(),
                request.getExpenseDescription(),
                request.getAmount(),
                request.getCategory(),
                request.getPaymentStatus(),
                request.getPaymentMethod(),
                request.getVendorName(),
                request.getDateIncurred(),
                request.getInvoiceUrl(),
                request.getReceiptUrl()
        );
        
        System.out.println("Expense created with ID: " + (expense != null ? expense.getId() : "NULL"));

        // Try budget recalculation
        try {
            budgetService.recalculateBudgetFromExpenses(expense.getEvent().getId());
            System.out.println("Budget recalculated successfully");
        } catch (RuntimeException e) {
            System.out.println("Budget recalculation skipped: " + e.getMessage());
        }

        ExpenseResponseDTO response = mapToExpenseResponseDTO(expense);
        System.out.println("Response DTO created: " + (response != null ? "SUCCESS" : "NULL"));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (RuntimeException e){
        System.out.println("ERROR creating expense: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
// ============================================================================
//                            GET MAPPING
// ============================================================================

    @GetMapping("/api/organizations/{orgId}/events/{eventId}/expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(@PathVariable Integer orgId,
                                                                @PathVariable Integer eventId,
                                                                @RequestParam(required = false) String category,
                                                                @RequestParam(required = false) String paymentStatus,
                                                                @RequestParam(required = false) String vendorName) {
        try {
            eventService.validateEventBelongsToOrg(eventId, orgId);

            List<Expense> expenses = expenseService.getFilteredExpensesByEvent(eventId, category, paymentStatus, vendorName);

            List<ExpenseResponseDTO> responses = expenses.stream()
                    .map(this::mapToExpenseResponseDTO)
                    .toList();

            return ResponseEntity.ok(responses);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

// ============================================================================
//                            PUT MAPPING
// ============================================================================

    @PutMapping("/api/organizations/{orgId}/events/{eventId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @PathVariable Integer expenseId,
            @RequestBody ExpenseUpdateRequestDTO request){
        try {
            eventService.validateEventBelongsToOrg(eventId, orgId);
            Expense expense = expenseService.validateExpenseExists(expenseId);
            if(!expense.getEvent().getId().equals(eventId)){
                throw new RuntimeException("Expense does not belong to this event");
            }
            if (request.getTitle() != null) {
                expenseService.updateExpenseTitle(expenseId, request.getTitle());
            }
            if (request.getExpenseDescription() != null) {
                expenseService.updateExpenseDescription(expenseId, request.getExpenseDescription());
            }
            if (request.getAmount() != null) {
                expenseService.updateExpenseAmount(expenseId, request.getAmount());
            }
            if (request.getCategory() != null) {
                expenseService.updateExpenseCategory(expenseId, request.getCategory());
            }
            if (request.getPaymentStatus() != null) {
                expenseService.updateExpensePaymentStatus(expenseId, request.getPaymentStatus());
            }
            if (request.getPaymentMethod() != null) {
                expenseService.updateExpensePaymentMethod(expenseId, request.getPaymentMethod());
            }
            if (request.getVendorName() != null) {
                expenseService.updateExpenseVendorName(expenseId, request.getVendorName());
            }
            if (request.getDateIncurred() != null) {
                expenseService.updateExpenseDateIncurred(expenseId, request.getDateIncurred());
            }
            if (request.getInvoiceUrl() != null) {
                expenseService.updateExpenseInvoiceUrl(expenseId, request.getInvoiceUrl());
            }
            if (request.getReceiptUrl() != null) {
                expenseService.updateExpenseReceiptUrl(expenseId, request.getReceiptUrl());
            }


            try {
                budgetService.recalculateBudgetFromExpenses(expense.getEvent().getId());
            } catch (RuntimeException e) {
                throw new RuntimeException("Error in recalculating budget remaining");
            }

            Expense updatedExpense = expenseService.getExpenseById(expenseId);
            ExpenseResponseDTO response = mapToExpenseResponseDTO(updatedExpense);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

// ============================================================================
//                            DELETE MAPPING
// ============================================================================

    @DeleteMapping("/api/organizations/{orgId}/events/{eventId}/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Integer orgId,
            @PathVariable Integer eventId,
            @PathVariable Integer expenseId) {
        try {
            eventService.validateEventBelongsToOrg(eventId, orgId);
            Expense expense = expenseService.validateExpenseExists(expenseId);
            if (!expense.getEvent().getId().equals(eventId)) {
                throw new RuntimeException("Expense does not belong to specified event");
            }

            Integer eventIdForRecalc = expense.getEvent().getId();
            expenseService.deleteExpense(expenseId);
            try {
                budgetService.recalculateBudgetFromExpenses(eventIdForRecalc);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error in recalculating budget remaining");
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

// ============================================================================
//                            DTO MAPPING
// ============================================================================

    private ExpenseResponseDTO mapToExpenseResponseDTO(Expense expense){
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setEventId(expense.getEvent().getId());
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setExpenseDescription(expense.getExpenseDescription());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setPaymentStatus(expense.getPaymentStatus());
        dto.setPaymentMethod(expense.getPaymentMethod());
        dto.setVendorName(expense.getVendorName());
        dto.setDateIncurred(expense.getDateIncurred());
        dto.setInvoiceUrl(expense.getInvoiceUrl());
        dto.setReceiptUrl(expense.getReceiptUrl());
        dto.setDateAdded(expense.getDateAdded());

        return dto;
    }














}


