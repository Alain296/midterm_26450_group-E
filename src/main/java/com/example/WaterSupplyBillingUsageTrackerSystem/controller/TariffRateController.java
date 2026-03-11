package com.example.WaterSupplyBillingUsageTrackerSystem.controller;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.TariffRate;
import com.example.WaterSupplyBillingUsageTrackerSystem.service.TariffRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TariffRate Controller
 */
@RestController
@RequestMapping("/api/tariff-rates")
public class TariffRateController {
    @Autowired
    private TariffRateService tariffRateService;

    @GetMapping
    public List<TariffRate> getAllTariffRates() {
        return tariffRateService.getAllTariffRates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffRate> getTariffRateById(@PathVariable Long id) {
        return tariffRateService.getTariffRateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<TariffRate> getTariffRatesByCategory(@PathVariable String category) {
        return tariffRateService.getTariffRatesByCategory(category);
    }

    @GetMapping("/active")
    public List<TariffRate> getActiveTariffRates() {
        return tariffRateService.getActiveTariffRates();
    }

    @GetMapping("/active/category/{category}")
    public ResponseEntity<TariffRate> getActiveTariffRateByCategory(@PathVariable String category) {
        return tariffRateService.getActiveTariffRateByCategory(category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createTariffRate(@RequestBody TariffRate tariffRate) {
        tariffRateService.saveTariffRate(tariffRate);
        return ResponseEntity.ok("Tariff rate saved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariffRate(@PathVariable Long id) {
        tariffRateService.deleteTariffRate(id);
        return ResponseEntity.noContent().build();
    }
}
