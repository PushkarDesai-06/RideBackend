package com.pushkar.ridebackend.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pushkar.ridebackend.dto.RideRequest;
import com.pushkar.ridebackend.exception.BadRequestException;
import com.pushkar.ridebackend.exception.NotFoundException;
import com.pushkar.ridebackend.model.Ride;
import com.pushkar.ridebackend.repository.RideRepository;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    // Create a new ride request (for USER role)
    public Ride createRide(String userId, RideRequest request) {
        Ride ride = new Ride(
                userId,
                request.getPickupLocation(),
                request.getDropLocation(),
                "REQUESTED",
                new Date());

        return rideRepository.save(ride);
    }

    // Get all pending ride requests (for DRIVER role)
    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    // Driver accepts a ride
    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED status");
        }

        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");

        return rideRepository.save(ride);
    }

    // Complete a ride
    public Ride completeRide(String rideId, String userId, String userRole) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in ACCEPTED status");
        }

        // Check if user is authorized (either passenger or driver)
        boolean isAuthorized = userId.equals(ride.getUserId()) ||
                              (userId.equals(ride.getDriverId()) && "ROLE_DRIVER".equals(userRole));

        if (!isAuthorized) {
            throw new BadRequestException("You are not authorized to complete this ride");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }    // Get user's rides
    public List<Ride> getUserRides(String userId) {
        return rideRepository.findByUserId(userId);
    }

    // Get ride by ID
    public Ride getRideById(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
    }
}
