package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table (name = "budgets")
public class Budget {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "budget_id", nullable = false)
    private Integer id;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "event_id")
    private Event event;

    @ColumnDefault ("0.00")
    @Column (name = "amount_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountTotal;

    @ColumnDefault ("0.00")
    @Column (name = "amount_spent", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountSpent;

    @ColumnDefault ("0.00")
    @Column (name = "amount_remaining", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountRemaining;

    @Column (name = "created_at")
    private Instant createdAt;

    @Column (name = "updated_at")
    private Instant updatedAt;

    public Budget(Integer id, Event event, BigDecimal amountTotal, BigDecimal amountSpent, BigDecimal amountRemaining, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.event = event;
        this.amountTotal = amountTotal;
        this.amountSpent = amountSpent;
        this.amountRemaining = amountRemaining;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Budget() {

    }
}