package com.heorhiizhemela.ratesservice.controller;

import com.heorhiizhemela.ratesservice.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CurrencyController.class)
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService currencyService;

    @Test
    public void testGetAllCurrencies() throws Exception {
        when(currencyService.getAllCurrencies()).thenReturn(Arrays.asList("USD", "EUR", "JPY"));

        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").value("USD"))
                .andExpect(jsonPath("$[1]").value("EUR"))
                .andExpect(jsonPath("$[2]").value("JPY"));
    }

    @Test
    public void testGetRate() throws Exception {
        BigDecimal rate = new BigDecimal("1.2345");
        when(currencyService.getExchangeRate("USD")).thenReturn(rate);

        mockMvc.perform(get("/currencies/USD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(rate.toString()));
    }

    @Test
    public void testAddCurrencyAndAudit() throws Exception {
        String newCurrency = "GBP";

        mockMvc.perform(post("/currencies")
                        .contentType("application/json")
                        .content(newCurrency))
                .andExpect(status().isOk());
    }
}
