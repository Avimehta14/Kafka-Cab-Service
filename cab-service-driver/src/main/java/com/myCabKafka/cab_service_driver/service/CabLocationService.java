package com.myCabKafka.cab_service_driver.service;

import com.myCabKafka.cab_service_driver.constants.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CabLocationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public boolean updateLocation(String location)
    {
        kafkaTemplate.send(AppConstant.CAB_LOCATION,location);
        return true;
    }

    public void simulateCabLocation() throws InterruptedException
    {
        int range = 50;
        while ( range>0 )
        {
            String location;

            if (Math.random() < 0.2)
            {
                location = "Invalid location of the Cab";
            }
            else
            {
                location = Math.random()+ "," + Math.random();
            }
            updateLocation(location);
            Thread.sleep(1000);
            range--;
        }
    }
}
