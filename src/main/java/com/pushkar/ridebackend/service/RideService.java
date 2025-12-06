package com.pushkar.ridebackend.service;

import com.pushkar.ridebackend.dto.RideRequest;
import com.pushkar.ridebackend.model.Ride;
import com.pushkar.ridebackend.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository){
        this.rideRepository = rideRepository;
    }

    public Ride createRide(RideRequest request) {
        Ride ride = new Ride(
                request.getUserId(),
                request.getDriverId(),
                request.getPickupLocation(),
                request.getDropLocation(),
                request.getStatus(),
                new Date()
        );

        return rideRepository.save(ride);
    }
}
