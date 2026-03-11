package com.example.WaterSupplyBillingUsageTrackerSystem.domain;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class WaterUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private int month;
    private int year;
    private double previousReading;
    private double currentReading;
    private double litersUsed;
    private LocalDate recordedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getPreviousReading() { return previousReading; }
    public void setPreviousReading(double previousReading) { this.previousReading = previousReading; }
    public double getCurrentReading() { return currentReading; }
    public void setCurrentReading(double currentReading) { this.currentReading = currentReading; }
    public double getLitersUsed() { return litersUsed; }
    public void setLitersUsed(double litersUsed) { this.litersUsed = litersUsed; }
    public LocalDate getRecordedDate() { return recordedDate; }
    public void setRecordedDate(LocalDate recordedDate) { this.recordedDate = recordedDate; }
}
