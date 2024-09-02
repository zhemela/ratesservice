package com.heorhiizhemela.ratesservice.service;

import com.heorhiizhemela.ratesservice.client.RateClient;
import com.heorhiizhemela.ratesservice.client.model.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatesServiceTest {

    @Mock
    private RateClient rateClient;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private RatesService ratesService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ratesService, "accessKey", "test-access-key");
        ReflectionTestUtils.setField(ratesService, "baseRate", "USD");
    }

    @Test
    void testGetActualRates_WithCurrencies_Success() {
        List<String> currencies = List.of("USD", "EUR");
        ExchangeRateResponse response = mock(ExchangeRateResponse.class);

        when(rateClient.getExchangeRates("test-access-key", "USD", currencies)).thenReturn(response);
        when(response.isSuccess()).thenReturn(true);
        when(response.getDate()).thenReturn("2024-08-31");

        Map<String, BigDecimal> result = ratesService.getActualRates(currencies);

        assertTrue(result.isEmpty()); // as method returns empty map on success
        verify(rateClient).getExchangeRates("test-access-key", "USD", currencies);
        verify(response).isSuccess();
    }

    @Test
    void testGetActualRates_EmptyCurrencies() {
        List<String> currencies = Collections.emptyList();

        Map<String, BigDecimal> result = ratesService.getActualRates(currencies);

        assertTrue(result.isEmpty());
        verify(rateClient, never()).getExchangeRates(anyString(), anyString(), anyList());
    }

    @Test
    void testGetActualRates_ExceptionDuringFetch() {
        List<String> currencies = List.of("USD", "EUR");

        when(rateClient.getExchangeRates("test-access-key", "USD", currencies)).thenThrow(new RuntimeException("API error"));

        Map<String, BigDecimal> result = ratesService.getActualRates(currencies);

        assertTrue(result.isEmpty());
        verify(rateClient).getExchangeRates("test-access-key", "USD", currencies);
        verifyNoMoreInteractions(rateClient);
    }
}
