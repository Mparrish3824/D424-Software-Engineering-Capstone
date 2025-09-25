package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseResponseDTO {
    private Integer id;
    private Integer eventId;
    private String title;
    private String expenseDescription;
    private BigDecimal amount;
    private String category;
    private String paymentStatus;
    private String paymentMethod;
    private String vendorName;
    private LocalDate dateIncurred;
    private String invoiceUrl;
    private String receiptUrl;
    private Instant dateAdded;

    public ExpenseResponseDTO(Integer id, Integer eventId, String title, String expenseDescription, BigDecimal amount, String category, String paymentStatus, String paymentMethod, String vendorName, LocalDate dateIncurred, String invoiceUrl, String receiptUrl, Instant dateAdded) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.expenseDescription = expenseDescription;
        this.amount = amount;
        this.category = category;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.vendorName = vendorName;
        this.dateIncurred = dateIncurred;
        this.invoiceUrl = invoiceUrl;
        this.receiptUrl = receiptUrl;
        this.dateAdded = dateAdded;
    }

    public ExpenseResponseDTO() {

    }
}
