package com.codingshuttle.project.uber.UberAppBackend.strategies.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.repositories.DriverRepository;
import com.codingshuttle.project.uber.UberAppBackend.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service //use where you need some business logics
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;
    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository
                .findTenNearestDrivers(
                        rideRequest.getPickUpLocation());
    }

}
