package com.heorhiizhemela.ratesservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RatesJobTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private RatesService ratesService;

    @InjectMocks
    private RatesJob ratesJob;

    @Test
    void testUpdateRates() {
        List<String> currencies = List.of("USD", "EUR");
        Map<String, BigDecimal> actualRates = Map.of("USD", BigDecimal.valueOf(1.0), "EUR", BigDecimal.valueOf(0.9));

        when(currencyService.getAllCurrencies()).thenReturn(currencies);
        when(ratesService.getActualRates(currencies)).thenReturn(actualRates);

        ratesJob.updateRates();

        verify(currencyService).getAllCurrencies();
        verify(ratesService).getActualRates(currencies);
        verify(currencyService).saveRates(actualRates);
    }

    @Test
    void testUpdateRates_EmptyCurrencies() {
        when(currencyService.getAllCurrencies()).thenReturn(List.of());

        ratesJob.updateRates();

        verify(currencyService).getAllCurrencies();
        verify(currencyService, never()).saveRates(anyMap());
    }
}
