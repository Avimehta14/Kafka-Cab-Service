package com.myCabKafka.cab_service_user.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DLQLocationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();
    private Queue<ConsumerRecord<String, String>> retryQueue = new ConcurrentLinkedQueue<>();

    private static final Logger logger = LoggerFactory.getLogger(DLQLocationService.class);
    private final int MAX_RETRIES = 2;

    @KafkaListener(topics = "cab-location-dlq", groupId = "dlq-group")
    public void processFailMessages(ConsumerRecord<String, String> record) {
        retryQueue.add(record); // Add to retry queue
    }

    @Scheduled(fixedRate = 5000) // Run every 5 seconds, adjust as necessary
    private void processRetryQueue() {
        ConsumerRecord<String, String> record;
        while ((record = retryQueue.poll()) != null) {
            String message = record.value();
            String key = record.key();
            int retries = retryCountMap.getOrDefault(key, 0);

            if (retries < MAX_RETRIES) {
                try {
                    logger.info("Retrying message: {}", message);
                    boolean success = processMessage(message);

                    if (success) {
                        retryCountMap.remove(key);
                    } else {
                        retryCountMap.put(key, retries + 1);
                        retryQueue.add(record); // Add back to the queue for the next retry
                    }
                } catch (Exception e) {
                    logger.error("Error processing message: {}", e.getMessage());
                    retryCountMap.put(key, retries + 1);
                    retryQueue.add(record); // Add back to the queue for the next retry
                }
            } else {
                logger.error("Max retries reached for message: {}", message);
                retryCountMap.remove(key);
            }
        }
    }

    private boolean processMessage(String message) {
        // Replace with actual processing logic
        return false;
    }
}

