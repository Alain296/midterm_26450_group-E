package com.example.WaterSupplyBillingUsageTrackerSystem.domain;

import jakarta.persistence.*;
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

    private double amount;
    private String status; // PAID/UNPAID
    private LocalDate dueDate;
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
