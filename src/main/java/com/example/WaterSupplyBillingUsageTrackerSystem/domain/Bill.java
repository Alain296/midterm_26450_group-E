package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "usage_id")
    private WaterUsage usage;

    @Min(value = 0, message = "Bill amount cannot be negative")
    private double amount;
    
    @NotBlank(message = "Bill status is required")
    private String status; // PAID/UNPAID
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    @NotNull(message = "Generated date is required")
    private LocalDate generatedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public WaterUsage getUsage() { return usage; }
    public void setUsage(WaterUsage usage) { this.usage = usage; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }
}
