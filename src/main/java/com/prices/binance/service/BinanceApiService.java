package com.prices.binance.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prices.binance.model.BinancePriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class BinanceApiService {

    @Value("${binance.api.url}")
    private String baseUrl;

    @Autowired
    private final SnsService snsService;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public BinanceApiService(SnsService snsService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.snsService = snsService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void handler() throws JsonProcessingException {
        BinancePriceResponse binancePriceResponse = getPrice("BTCUSDT");
        String snsPayload = objectMapper.writeValueAsString(binancePriceResponse);
        log.info(String.valueOf(snsPayload));
        snsService.pubTopic(snsPayload);
    }

    public BinancePriceResponse getPrice(String symbol) {

        String url = String.format("%s/ticker/price?symbol=%s", baseUrl, symbol);

        return restTemplate.getForObject(url, BinancePriceResponse.class);

    }

}
