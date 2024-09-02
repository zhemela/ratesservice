package com.heorhiizhemela.ratesservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatesJob {
    private final CurrencyService currencyService;
    private final RatesService ratesService;

    @Scheduled(fixedRate = 3600000, initialDelay = 0)
    public void updateRates() {
        List<String> currencies = currencyService.getAllCurrencies();
        if (currencies.isEmpty()) {
            return;
        }
        Map<String, BigDecimal> actualRates = ratesService.getActualRates(currencies);
        currencyService.saveRates(actualRates);
    }
}
