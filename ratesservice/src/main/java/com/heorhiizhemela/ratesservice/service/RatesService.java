package com.heorhiizhemela.ratesservice.service;

import com.heorhiizhemela.ratesservice.client.RateClient;
import com.heorhiizhemela.ratesservice.client.model.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesService {
    private final RateClient rateClient;
    @Value("${fixer.api.key}")
    private String accessKey;
    @Value("${fixer.api.base}")
    private String baseRate;

    public Map<String, BigDecimal> getActualRates(List<String> currencies) {
        if (currencies.isEmpty()) {
            log.info("Currency list is empty, skipping rates job...");
            return Collections.emptyMap();
        }
        try {
            ExchangeRateResponse response = rateClient.getExchangeRates(accessKey, baseRate, currencies);
            if (response.isSuccess()) {
                log.info("Exchange rates updated successfully at " + response.getDate());
                return response.getRates();
            } else {
                throw new RuntimeException("Failed to fetch exchange rates");
            }
        } catch (Exception e) {
            log.error("Failed to fetch exchange rates");
            return Collections.emptyMap();
        }
    }
}
