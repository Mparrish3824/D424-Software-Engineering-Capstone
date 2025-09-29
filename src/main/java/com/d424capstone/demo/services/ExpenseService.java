package com.d424capstone.demo.services;

import com.d424capstone.demo.dto.ExpenseResponseDTO;
import com.d424capstone.demo.entities.Event;
import com.d424capstone.demo.entities.Expense;
import com.d424capstone.demo.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EventService eventService;


// ============================================================================
//                         Validation Methods
// ============================================================================

    public Expense validateExpenseExists(Integer expenseId) {
        Optional<Expense> expense = expenseRepository.findById(expenseId);
        if (expense.isEmpty()) {
            throw new RuntimeException("Expense not found with ID: " + expenseId);
        }
        return expense.get();
    }

    public void validateExpenseBelongsToEvent(Integer expenseId, Integer eventId) {
        Expense expense = validateExpenseExists(expenseId);
        if(!expense.getEvent().getId().equals(eventId)){
            throw new RuntimeException("Expense ID " + expenseId + " does not belong to event " + eventId);
        }
    }


// ============================================================================
//                            CRUD METHODS
// ============================================================================

    @Transactional
    public Expense createExpense(Integer eventId, String title, String expenseDescription,
                                 BigDecimal amount, String category, String paymentStatus,
                                 String paymentMethod, String vendorName, LocalDate dateIncurred,
                                 String invoiceUrl, String receiptUrl) {

        Event event = eventService.validateEventExists(eventId);

        Expense expense = new Expense();
        expense.setEvent(event);
        expense.setTitle(title);
        expense.setExpenseDescription(expenseDescription);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setPaymentStatus(paymentStatus != null ? paymentStatus : "pending");
        expense.setPaymentMethod(paymentMethod);
        expense.setVendorName(vendorName);
        expense.setDateIncurred(dateIncurred != null ? dateIncurred : LocalDate.now());        
        expense.setInvoiceUrl(invoiceUrl);
        expense.setReceiptUrl(receiptUrl);
        expense.setDateAdded(Instant.now());

        Expense savedExpense = expenseRepository.save(expense);

        return savedExpense;
    }

    @Transactional
    public Expense updateExpenseAmount(Integer expenseId, BigDecimal newAmount) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setAmount(newAmount);

        Expense savedExpense = expenseRepository.save(expense);

        return savedExpense;
    }

    public Expense updateExpensePaymentStatus(Integer expenseId, String paymentStatus) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setPaymentStatus(paymentStatus);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseDetails(Integer expenseId, String title, String expenseDescription,
                                        String category, String paymentMethod, String vendorName,
                                        LocalDate dateIncurred, String invoiceUrl, String receiptUrl) {
        Expense expense = validateExpenseExists(expenseId);

        if (title != null) expense.setTitle(title);
        if (expenseDescription != null) expense.setExpenseDescription(expenseDescription);
        if (category != null) expense.setCategory(category);
        if (paymentMethod != null) expense.setPaymentMethod(paymentMethod);
        if (vendorName != null) expense.setVendorName(vendorName);
        if (dateIncurred != null) expense.setDateIncurred(dateIncurred);
        if (invoiceUrl != null) expense.setInvoiceUrl(invoiceUrl);
        if (receiptUrl != null) expense.setReceiptUrl(receiptUrl);

        return expenseRepository.save(expense);
    }

    public Expense updateExpenseTitle(Integer expenseId, String title) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setTitle(title);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseDescription(Integer expenseId, String description) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setExpenseDescription(description);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseCategory(Integer expenseId, String category) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setCategory(category);
        return expenseRepository.save(expense);
    }

    public Expense updateExpensePaymentMethod(Integer expenseId, String paymentMethod) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setPaymentMethod(paymentMethod);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseVendorName(Integer expenseId, String vendorName) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setVendorName(vendorName);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseDateIncurred(Integer expenseId, LocalDate dateIncurred) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setDateIncurred(dateIncurred);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseInvoiceUrl(Integer expenseId, String invoiceUrl) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setInvoiceUrl(invoiceUrl);
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseReceiptUrl(Integer expenseId, String receiptUrl) {
        Expense expense = validateExpenseExists(expenseId);
        expense.setReceiptUrl(receiptUrl);
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Integer expenseId) {
        Expense expense = validateExpenseExists(expenseId);
        Integer eventId = expense.getEvent().getId();

        expenseRepository.delete(expense);

    }



// ============================================================================
//                         LOOKUP METHODS
// ============================================================================

    public Expense getExpenseById(Integer expenseId) {
        return validateExpenseExists(expenseId);
    }

    public List<Expense> getExpensesByEventId(Integer eventId) {
        return expenseRepository.findAllByEvent_Id(eventId);
    }

    public List<Expense> getExpensesByOrgId(Integer orgId) {
        return expenseRepository.findAllByOrgId(orgId);
    }

    public List<Expense> getExpensesByEventAndCategory(Integer eventId, String category) {
        return expenseRepository.findAllByEvent_IdAndCategory(eventId, category);
    }

    public List<Expense> getExpensesByEventAndPaymentStatus(Integer eventId, String paymentStatus) {
        return expenseRepository.findAllByEvent_IdAndPaymentStatus(eventId, paymentStatus);
    }

    public List<Expense> getExpensesByEventAndVendor(Integer eventId, String vendorName) {
        return expenseRepository.findAllByEvent_IdAndVendorName(eventId, vendorName);
    }

    public BigDecimal getTotalExpensesByEventId(Integer eventId) {
        return expenseRepository.findTotalExpensesByEventId(eventId);
    }

    public BigDecimal getPaidExpensesByEventId(Integer eventId) {
        return expenseRepository.findPaidExpensesByEventId(eventId);
    }

    public BigDecimal getPendingExpensesByEventId(Integer eventId) {
        return expenseRepository.findPendingExpensesByEventId(eventId);
    }

// ============================================================================
//                         Filter Method
// ============================================================================

    public List<Expense> getFilteredExpensesByEvent(Integer eventId, String category, String paymentStatus, String vendorName) {

        if (category != null && paymentStatus != null && vendorName != null) {
            return expenseRepository.findAllByEvent_IdAndCategoryAndPaymentStatusAndVendorName(eventId, category, paymentStatus, vendorName);
        }
        else if (category != null && paymentStatus != null) {
            return expenseRepository.findAllByEvent_IdAndCategoryAndPaymentStatus(eventId, category, paymentStatus);
        }
        else if (category != null && vendorName != null) {
            return expenseRepository.findAllByEvent_IdAndCategoryAndVendorName(eventId, category, vendorName);
        }
        else if (paymentStatus != null && vendorName != null) {
            return expenseRepository.findAllByEvent_IdAndPaymentStatusAndVendorName(eventId, paymentStatus, vendorName);
        }
        else if (category != null) {
            return getExpensesByEventAndCategory(eventId, category);
        }
        else if (paymentStatus != null) {
            return getExpensesByEventAndPaymentStatus(eventId, paymentStatus);
        }
        else if (vendorName != null) {
            return getExpensesByEventAndVendor(eventId, vendorName);
        }
        else {
            return getExpensesByEventId(eventId);
        }
    }
// ============================================================================
//                         DTO METHODS
// ============================================================================

    @Transactional(readOnly = true)
    protected ExpenseResponseDTO mapToExpenseResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getEvent().getId(),
                expense.getTitle(),
                expense.getExpenseDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getPaymentStatus(),
                expense.getPaymentMethod(),
                expense.getVendorName(),
                expense.getDateIncurred(),
                expense.getInvoiceUrl(),
                expense.getReceiptUrl(),
                expense.getDateAdded()
        );
    }













}

