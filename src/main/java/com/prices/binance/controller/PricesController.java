package com.prices.binance.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prices.binance.model.BinancePriceResponse;
import com.prices.binance.service.BinanceApiService;
import com.prices.binance.service.SnsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class PricesController {

    @Value("${crypto.list}")
    private String stringCryptoList;

    private final ObjectMapper objectMapper;

    @Autowired
    private final BinanceApiService binanceApiService;

    @Autowired
    private final SnsService snsService;

    public PricesController(ObjectMapper objectMapper, BinanceApiService binanceApiService, SnsService snsService) {
        this.objectMapper = objectMapper;
        this.binanceApiService = binanceApiService;
        this.snsService = snsService;
    }

    @PostConstruct
    public List<PublishResponse> handler() throws JsonProcessingException {

        List<PublishResponse> publishResponseList = new ArrayList<>();
        String[] cryptos = getCryptoNames();
        for (String crypto : cryptos) {
            BinancePriceResponse binancePriceResponse = binanceApiService.getPrice(crypto);
            String snsPayload = objectMapper.writeValueAsString(binancePriceResponse);
            log.info(String.valueOf(snsPayload));
            PublishResponse publishResponse = snsService.pubTopic(snsPayload);
            if (publishResponse != null){
                publishResponseList.add(publishResponse);
            }
        }

        return publishResponseList;
    }

    private String[] getCryptoNames() {
        return stringCryptoList.split(",");
    }
}

