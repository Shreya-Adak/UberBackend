package com.codingshuttle.project.uber.UberAppBackend.strategies.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.repositories.DriverRepository;
import com.codingshuttle.project.uber.UberAppBackend.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional()
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

   private final DriverRepository driverRepository;



    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        return driverRepository
                .findTenNearbyTopRatedDrivers(
                        rideRequest.getPickUpLocation());
    }
}
