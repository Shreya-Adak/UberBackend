package com.codingshuttle.project.uber.UberAppBackend.strategies;

import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;

public interface RideFareCalculationStrategy {
    double RIDE_FARE_MULTIPLIER = 8.88;

    Double calculateFare(RideRequest rideRequest);
//    double RIDE_FARE_MULTIPLIER = 10;
//
//    double calculateFare(RideRequest rideRequest);
}
