package com.heorhiizhemela.ratesservice.client;

import com.heorhiizhemela.ratesservice.client.model.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "exchangeRateClient")
public interface RateClient {

    @GetMapping("/latest")
    ExchangeRateResponse getExchangeRates(
            @RequestParam("access_key") String accessKey,
            @RequestParam("base") String base,
            @RequestParam("symbols") List<String> symbols
    );
}
