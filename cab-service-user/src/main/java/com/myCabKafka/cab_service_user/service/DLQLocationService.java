package com.myCabKafka.cab_service_user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DLQLocationService {

    private static final Logger logger = LoggerFactory.getLogger(DLQLocationService.class);

    @KafkaListener(topics = "cab-location-dlq",groupId = "dlq-group")
    public void processFailMessages(String location)
    {
        System.out.println("Failed Locations after retires"+ location);
    }
}
