package com.prices.binance.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.UUID;

@Service
@Slf4j
@Data
public class SnsService {

    @Value("${sns.arn}")
    private String topicArn;

    private SnsClient snsClient;

    public SnsService() {
        this.topicArn = topicArn;
        this.snsClient = (
                SnsClient.builder()
                        .region(Region.SA_EAST_1)
                        .build()
        );
    }

    public void pubTopic(String message) {

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .messageGroupId(UUID.randomUUID().toString())
                    .messageDeduplicationId(UUID.randomUUID().toString())
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());
            snsClient.close();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}
