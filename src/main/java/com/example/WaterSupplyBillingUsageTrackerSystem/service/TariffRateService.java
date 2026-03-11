package com.example.WaterSupplyBillingUsageTrackerSystem.service;

import com.example.WaterSupplyBillingUsageTrackerSystem.domain.TariffRate;
import com.example.WaterSupplyBillingUsageTrackerSystem.repository.TariffRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TariffRate Service
 * Manages water pricing information
 */
@Service
public class TariffRateService {
    @Autowired
    private TariffRateRepository tariffRateRepository;

    public TariffRate saveTariffRate(TariffRate tariffRate) {
        return tariffRateRepository.save(tariffRate);
    }

    public Optional<TariffRate> getTariffRateById(Long id) {
        return tariffRateRepository.findById(id);
    }

    public List<TariffRate> getTariffRatesByCategory(String category) {
        return tariffRateRepository.findByCategory(category);
    }

    public List<TariffRate> getActiveTariffRates() {
        return tariffRateRepository.findByActiveTrue();
    }

    public Optional<TariffRate> getActiveTariffRateByCategory(String category) {
        return tariffRateRepository.findByCategoryAndActiveTrue(category);
    }

    public List<TariffRate> getAllTariffRates() {
        return tariffRateRepository.findAll();
    }

    public void deleteTariffRate(Long id) {
        tariffRateRepository.deleteById(id);
    }
}
