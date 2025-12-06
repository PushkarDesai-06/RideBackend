package com.pushkar.ridebackend.controller;


import com.pushkar.ridebackend.dto.RideRequest;
import com.pushkar.ridebackend.model.Ride;
import com.pushkar.ridebackend.service.RideService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public Ride createRide(@Valid @RequestBody RideRequest request){
        return rideService.createRide(request);
    }
}
