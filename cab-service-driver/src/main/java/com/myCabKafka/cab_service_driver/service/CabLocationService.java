package com.myCabKafka.cab_service_driver.service;

import com.myCabKafka.cab_service_driver.constants.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CabLocationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void updateLocation(String location)
    {
        kafkaTemplate.send(AppConstant.CAB_LOCATION,location);
    }

    public void simulateCabLocation() throws InterruptedException
    {
        int range = 50;
        while ( range >= 0 )
        {
            String location;
            double randomLocation = Math.random();
            location = String.valueOf(randomLocation);
            updateLocation(location);
            Thread.sleep(1000);
            range--;
        }
    }
}
