package com.myCabKafka.cab_service_user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserLocationService {

    private static final Logger logger = LoggerFactory.getLogger(UserLocationService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private LocationStorage locationStorage;

    @KafkaListener(topics= "cab-location",groupId = "user-group")
    public void cabLocation(String location)
    {
        try
        {
            if (isValidLocation(location))
            {
                logger.info("Received Location: {}", location);
                locationStorage.setLatestLocation(location);
            } else {
                logger.error("Invalid Location format: {}", location);
                sendToDLQ(location);
            }
        } catch (Exception e) {
            logger.error("Error processing location update: {}", e.getMessage());
            sendToDLQ(location);
        }
    }

    private void sendToDLQ(String location)
    {
        kafkaTemplate.send("cab-location-dlq",location);
    }

    private boolean isValidLocation(String location)
    {
        double val = Double.parseDouble(location);
        return location != null && val > 0.2;
    }
}
