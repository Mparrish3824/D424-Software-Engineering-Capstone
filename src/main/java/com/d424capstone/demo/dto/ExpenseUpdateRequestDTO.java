package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseUpdateRequestDTO {
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

    public ExpenseUpdateRequestDTO() {
    }
}
