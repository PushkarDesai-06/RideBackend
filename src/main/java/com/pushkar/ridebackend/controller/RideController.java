package com.pushkar.ridebackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pushkar.ridebackend.dto.RideRequest;
import com.pushkar.ridebackend.model.Ride;
import com.pushkar.ridebackend.service.RideService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: Create a ride request
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(
            @Valid @RequestBody RideRequest request,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        Ride ride = rideService.createRide(userId, request);
        return ResponseEntity.ok(ride);
    }

    // USER: Get their own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getUserRides(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<Ride> rides = rideService.getUserRides(userId);
        return ResponseEntity.ok(rides);
    }

    // DRIVER: Get all pending ride requests
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> getPendingRides() {
        List<Ride> rides = rideService.getPendingRides();
        return ResponseEntity.ok(rides);
    }

    // DRIVER: Accept a ride
    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(
            @PathVariable String rideId,
            Authentication authentication) {
        String driverId = (String) authentication.getPrincipal();
        Ride ride = rideService.acceptRide(rideId, driverId);
        return ResponseEntity.ok(ride);
    }

    // USER or DRIVER: Complete a ride
    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(
            @PathVariable String rideId,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        String userRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        Ride ride = rideService.completeRide(rideId, userId, userRole);
        return ResponseEntity.ok(ride);
    }
}
