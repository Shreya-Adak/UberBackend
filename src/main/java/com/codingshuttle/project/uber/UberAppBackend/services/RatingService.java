package com.codingshuttle.project.uber.UberAppBackend.services;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RiderDto;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);
}
