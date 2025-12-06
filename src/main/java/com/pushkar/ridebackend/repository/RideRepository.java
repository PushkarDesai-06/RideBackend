package com.pushkar.ridebackend.repository;

import com.pushkar.ridebackend.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RideRepository extends MongoRepository<Ride,String> {
}
