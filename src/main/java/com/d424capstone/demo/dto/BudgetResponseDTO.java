package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class BudgetResponseDTO {

    private Integer budgetId;
    private Integer eventId;
    private String eventName;
    private BigDecimal amountTotal;
    private BigDecimal amountSpent;
    private BigDecimal amountRemaining;
    private BigDecimal percentageUsed;
    private Boolean isOverBudget;
    private Instant createdAt;
    private Instant updatedAt;

    public BudgetResponseDTO(Integer budgetId, Integer eventId, String eventName, BigDecimal amountTotal, BigDecimal amountSpent, BigDecimal amountRemaining, BigDecimal percentageUsed, Boolean isOverBudget, Instant createdAt, Instant updatedAt) {
        this.budgetId = budgetId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.amountTotal = amountTotal;
        this.amountSpent = amountSpent;
        this.amountRemaining = amountRemaining;
        this.percentageUsed = percentageUsed;
        this.isOverBudget = isOverBudget;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BudgetResponseDTO() {

    }
}
