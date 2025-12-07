package com.pushkar.ridebackend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pushkar.ridebackend.model.Ride;

public interface RideRepository extends MongoRepository<Ride, String> {
  List<Ride> findByStatus(String status);

  List<Ride> findByUserId(String userId);
}
