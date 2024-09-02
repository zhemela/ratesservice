package com.heorhiizhemela.ratesservice.repository;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CurrencyStorage {
    private final ConcurrentMap<String, BigDecimal> exchangeRatesMap = new ConcurrentHashMap<>();

    public Optional<BigDecimal> getExchangeRate(String currency) {
        return Optional.ofNullable(exchangeRatesMap.get(currency));
    }

    public String addCurrency(String currency, BigDecimal rate) {
        if (exchangeRatesMap.putIfAbsent(currency, rate) != null) {
            throw new RuntimeException("Currency already exists: " + currency);
        }
        return currency;
    }

    public List<String> getAllCurrencies() {
        return List.copyOf(exchangeRatesMap.keySet());
    }

    public Map<String, BigDecimal> saveRates(Map<String, BigDecimal> rates) {
        exchangeRatesMap.putAll(rates);
        return Collections.unmodifiableMap(rates);
    }
}
