package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name = "expenses")
public class Expense {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "expense_id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "event_id")
    private Event event;


    @Column (name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    @Column (name = "expense_description")
    private String expenseDescription;

    @Column (name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Lob
    @Column (name = "invoice_url")
    private String invoiceUrl;

    @Lob
    @Column (name = "receipt_url")
    private String receiptUrl;


    @Column (name = "category", nullable = false)
    private String category;

    @ColumnDefault ("'pending'")
    @Column (name = "payment_status")
    private String paymentStatus;

    @Column (name = "payment_method")
    private String paymentMethod;

    @Column (name = "vendor_name", length = 100)
    private String vendorName;

    @Column (name = "date_incurred", nullable = false)
    private LocalDate dateIncurred;

    @Column (name = "date_added")
    private Instant dateAdded;


    public Expense(Integer id, Event event, String title, String expenseDescription, BigDecimal amount, String invoiceUrl, String receiptUrl, String category, String paymentStatus, String paymentMethod, String vendorName, LocalDate dateIncurred, Instant dateAdded) {
        this.id = id;
        this.event = event;
        this.title = title;
        this.expenseDescription = expenseDescription;
        this.amount = amount;
        this.invoiceUrl = invoiceUrl;
        this.receiptUrl = receiptUrl;
        this.category = category;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.vendorName = vendorName;
        this.dateIncurred = dateIncurred;
        this.dateAdded = dateAdded;
    }

    public Expense() {

    }
}