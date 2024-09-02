package com.heorhiizhemela.ratesservice.service;

import com.heorhiizhemela.ratesservice.audit.entity.ActionType;
import com.heorhiizhemela.ratesservice.audit.service.AuditService;
import com.heorhiizhemela.ratesservice.repository.CurrencyStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyStorage storage;
    private final AuditService auditService;

    public List<String> getAllCurrencies() {
        return storage.getAllCurrencies();
    }

    public BigDecimal getExchangeRate(String currency) {
        return storage.getExchangeRate(currency)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + currency));
    }

    public String addCurrency(String currency) {
        String savedCurrency = storage.addCurrency(currency, BigDecimal.ZERO);
        auditService.auditRateChange(currency, null, ActionType.CREATE);
        return savedCurrency;
    }

    public Map<String, BigDecimal> saveRates(Map<String, BigDecimal> rates) {
        List<String> existingCurrencies = storage.getAllCurrencies();
        Map<String, BigDecimal> savedRates = storage.saveRates(rates);

        savedRates.forEach((currency, rate) -> {
            ActionType actionType = existingCurrencies.contains(currency) ? ActionType.UPDATE : ActionType.CREATE;
            auditService.auditRateChange(currency, rate, actionType);
        });

        return savedRates;
    }
}
