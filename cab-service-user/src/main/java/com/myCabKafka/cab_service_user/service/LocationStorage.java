package com.myCabKafka.cab_service_user.service;

import org.springframework.stereotype.Component;

@Component
public class LocationStorage {

    private String latestLocation = "No data available";

    public String getLatestLocation() {
        return latestLocation;
    }

    public void setLatestLocation(String latestLocation) {
        this.latestLocation = latestLocation;
    }
}
