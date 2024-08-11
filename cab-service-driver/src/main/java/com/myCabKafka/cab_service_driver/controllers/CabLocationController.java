package com.myCabKafka.cab_service_driver.controllers;

import com.myCabKafka.cab_service_driver.service.CabLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/location")
public class CabLocationController {

    @Autowired
    private CabLocationService cabLocationService;

    @PutMapping
    public ResponseEntity updateLocation()
    {
        int range = 100;
        while( range> 0)
        {
            System.out.println(Math.random()+","+Math.random());
            range--;
        }

        return new ResponseEntity<>(Map.of("message","location updated"), HttpStatus.OK);
    }
}