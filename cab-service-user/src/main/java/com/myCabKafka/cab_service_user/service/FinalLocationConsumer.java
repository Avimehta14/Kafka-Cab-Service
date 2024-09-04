package com.myCabKafka.cab_service_user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class FinalLocationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(FinalLocationConsumer.class);

    @KafkaListener(topics="final-topic",groupId = "user-group")
    public void finalListenerTopic(String location)
    {
        System.out.println("Final location received in final topic: "+location);
        logger.info("Final location received in final topic:" +location);
    }
}
