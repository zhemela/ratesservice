package com.heorhiizhemela.ratesservice.service;

import com.heorhiizhemela.ratesservice.audit.entity.ActionType;
import com.heorhiizhemela.ratesservice.audit.service.AuditService;
import com.heorhiizhemela.ratesservice.repository.CurrencyStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyStorage storage;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private CurrencyService currencyService;

    @Captor
    private ArgumentCaptor<String> currencyCaptor;

    @Captor
    private ArgumentCaptor<BigDecimal> rateCaptor;

    @Captor
    private ArgumentCaptor<ActionType> actionTypeCaptor;


    @Test
    void testGetAllCurrencies() {
        List<String> currencies = List.of("USD", "EUR", "GBP");
        when(storage.getAllCurrencies()).thenReturn(currencies);

        List<String> result = currencyService.getAllCurrencies();

        assertEquals(currencies, result);
        verify(storage).getAllCurrencies();
    }

    @Test
    void testGetExchangeRate_CurrencyExists() {
        String currency = "USD";
        BigDecimal rate = BigDecimal.valueOf(1.0);
        when(storage.getExchangeRate(currency)).thenReturn(Optional.of(rate));

        BigDecimal result = currencyService.getExchangeRate(currency);

        assertEquals(rate, result);
        verify(storage).getExchangeRate(currency);
    }

    @Test
    void testGetExchangeRate_CurrencyNotFound() {
        String currency = "XYZ";
        when(storage.getExchangeRate(currency)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyService.getExchangeRate(currency)
        );

        assertEquals("Currency not found: XYZ", exception.getMessage());
        verify(storage).getExchangeRate(currency);
    }

    @Test
    void testAddCurrency() {
        String currency = "USD";
        when(storage.addCurrency(currency, BigDecimal.ZERO)).thenReturn(currency);

        String result = currencyService.addCurrency(currency);

        assertEquals(currency, result);
        verify(storage).addCurrency(currency, BigDecimal.ZERO);
        verify(auditService).auditRateChange(currencyCaptor.capture(), rateCaptor.capture(), actionTypeCaptor.capture());

        assertEquals(currency, currencyCaptor.getValue());
        assertNull(rateCaptor.getValue());
        assertEquals(ActionType.CREATE, actionTypeCaptor.getValue());
    }

    @Test
    void testSaveRates() {
        Map<String, BigDecimal> rates = Map.of("USD", BigDecimal.valueOf(1.0), "EUR", BigDecimal.valueOf(0.9));
        List<String> existingCurrencies = List.of("USD");
        when(storage.getAllCurrencies()).thenReturn(existingCurrencies);
        when(storage.saveRates(rates)).thenReturn(rates);

        Map<String, BigDecimal> result = currencyService.saveRates(rates);

        assertEquals(rates, result);
        verify(storage).getAllCurrencies();
        verify(storage).saveRates(rates);
        verify(auditService, times(2)).auditRateChange(currencyCaptor.capture(), rateCaptor.capture(), actionTypeCaptor.capture());

        List<String> capturedCurrencies = currencyCaptor.getAllValues();
        List<BigDecimal> capturedRates = rateCaptor.getAllValues();
        List<ActionType> capturedActions = actionTypeCaptor.getAllValues();

        assertTrue(capturedCurrencies.containsAll(List.of("USD", "EUR")));
        assertTrue(capturedRates.containsAll(List.of(BigDecimal.valueOf(1.0), BigDecimal.valueOf(0.9))));
        assertTrue(capturedActions.containsAll(List.of(ActionType.UPDATE, ActionType.CREATE)));
    }
}
