package com.myCabKafka.cab_service_user.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class DLQLocationService {

    private static final Logger logger = LoggerFactory.getLogger(DLQLocationService.class);

    @Autowired
    private UserLocationService userLocationService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String originalTopic = "cab-location";
    private final String dlqTopic = "cab-location-dlq";


    private final Queue<String> retryQueue = new LinkedList<>();

    @KafkaListener(topics = "cab-location-dlq", groupId = "dlq-group")
    public void handleDlqMessages(ConsumerRecord<String, String> record) {
        String location = record.value();
        retryQueue.add(location);
        System.out.println("Retrying all failed events in DLQ !!");
        logger.info("Retruing in DLQ nOw");
        retryMessages();
    }

    @Scheduled(fixedRate = 10000)
    public void retryMessages() {
        while (!retryQueue.isEmpty()) {
            String location = retryQueue.poll();

            try {
                if (userLocationService.isValidLocation(location)) {
                    logger.info(" Messaged processd from the DLQ on retrying");
                } else {
                    double val = Double.parseDouble(location);
                    val = val+1;
                    location = String.valueOf(val);
                    logger.error("Still Invalid Location format: {}", location);
                    kafkaTemplate.send(dlqTopic, location);
                }
            } catch (Exception e) {
                logger.error("Catch : Error processing DLQ message: {}", e.getMessage());
                kafkaTemplate.send(dlqTopic, location);
            }
        }
    }
}

