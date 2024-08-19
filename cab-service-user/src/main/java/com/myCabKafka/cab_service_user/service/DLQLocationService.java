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

@Service
public class DLQLocationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Map<String, Integer> retryCountMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DLQLocationService.class);
    private final int MAX_RETRIES = 2;

    @KafkaListener(topics = "cab-location-dlq",groupId = "dlq-group")
    public void processFailMessages(ConsumerRecord<String , String> record)
    {
        String message =record.value();
        String key = record.key();

        int retires = retryCountMap.getOrDefault(key,0);

        if (retires < MAX_RETRIES)
        {
            retryProcessing(message,key);
        }
        else
        {
            esclate(message,key);
        }
    }

    @Scheduled(fixedRate = 30000)
    private void retryProcessing(String message, String key)
    {
        try {

            System.out.println("Retrying the message" + message);
            boolean success= processMessage(message);

            if (success)
            {
                retryCountMap.remove(key);
            }
            else
            {
                retryCountMap.put(key, retryCountMap.getOrDefault(key,0)+1);
            }
        }
        catch (Exception e)
        {
            retryCountMap.put(key, retryCountMap.getOrDefault(key,0)+1);
        }
    }

    private boolean processMessage(String message)
    {
        return false;
    }

    // Custom retry of failed messages
    private void esclate(String message , String key)
    {
        System.err.println("Escalating message"+ message);
        kafkaTemplate.send("escalated-dlq",key,message);
        retryCountMap.remove(key);
    }
}
