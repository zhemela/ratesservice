package com.heorhiizhemela.ratesservice.controller;

import com.heorhiizhemela.ratesservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<String>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{currency}")
    public ResponseEntity<BigDecimal> getRate(@PathVariable String currency) {
        return ResponseEntity.ok(currencyService.getExchangeRate(currency));
    }

    @PostMapping
    public ResponseEntity<String> addCurrency(@RequestBody String currency) {
        return ResponseEntity.ok(currencyService.addCurrency(currency));
    }
}
