package com.prices.binance.service;

import com.prices.binance.model.BinancePriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BinanceApiService {

    @Value("${binance.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public BinanceApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BinancePriceResponse getPrice(String symbol) {

        String url = String.format("%s/ticker/price?symbol=%s", baseUrl, symbol);
        return restTemplate.getForObject(url, BinancePriceResponse.class);

    }

}
