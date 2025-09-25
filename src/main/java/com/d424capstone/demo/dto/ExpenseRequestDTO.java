package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequestDTO {
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

    public ExpenseRequestDTO(String title, String expenseDescription, BigDecimal amount, String category, String paymentStatus, String paymentMethod, String vendorName, LocalDate dateIncurred, String invoiceUrl, String receiptUrl) {
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
    }

    public ExpenseRequestDTO() {
    }
}
