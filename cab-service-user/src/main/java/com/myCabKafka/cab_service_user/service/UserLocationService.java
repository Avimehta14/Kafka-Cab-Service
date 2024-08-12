package com.myCabKafka.cab_service_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserLocationService {

    @Autowired
    private LocationStorage locationStorage;

    @KafkaListener(topics= "cab-location",groupId = "user-group")
    public void cabLocation(String location)
    {
        System.out.println("Received Location:"+ location);
        locationStorage.setLatestLocation(location);
    }

}
