package com.myCabKafka.cab_service_user.controllers;

import com.myCabKafka.cab_service_user.service.LocationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserCabLocationController
{
    @Autowired
    private LocationStorage locationStorage;
    
    @GetMapping("/driver-location")
    public String getDriverLocation(Model model)
    {
        String location = locationStorage.getLatestLocation();
        model.addAttribute("location",location);
        return "location";
    }
}
