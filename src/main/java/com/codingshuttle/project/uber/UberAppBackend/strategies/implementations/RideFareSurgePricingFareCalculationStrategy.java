package com.codingshuttle.project.uber.UberAppBackend.strategies.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.services.DistanceService;
import com.codingshuttle.project.uber.UberAppBackend.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    private final DistanceService distanceService;
    private static final double SURGE_FACTOR = 2;

    @Override
    public Double calculateFare(RideRequest rideRequest) {
//        double distance = distanceService.calculateDistance(
//                rideRequest.getPickUpLocation(),
//                rideRequest.getDropOffLocation());

//        return distance*RIDE_FARE_MULTIPLIER*SURGE_FACTOR;
        return distanceService.calculateDistance(rideRequest.getPickUpLocation(),rideRequest.getDropOffLocation());

    }
}
