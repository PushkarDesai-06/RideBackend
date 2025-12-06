package com.pushkar.ridebackend.repository;

import com.pushkar.ridebackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByUsername(String id);
}
