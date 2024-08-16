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
            double randomLocation = Math.random();

            if (randomLocation < 0.2)
            {
                location = String.valueOf(randomLocation);
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
