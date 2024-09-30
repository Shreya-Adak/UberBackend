package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RiderDto;
import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rating;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.RuntimeConflictExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.DriverRepository;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RatingRepository;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RiderRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImplement implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository
                .findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundExceptions("RATING NOT FOUND FOR RIDE WITH ID: "+ride.getId()));

        if(ratingObj.getDriverRating() != null )throw new RuntimeConflictExceptions("DRIVER HAS ALREADY BEEN RATED");
        ratingObj.setDriverRating(rating);

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        driver.setRating(newRating);

        Driver driver1 = driverRepository.save(driver);
        return modelMapper.map(driver1, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository
                .findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundExceptions("RATING NOT FOUND FOR RIDE WITH ID: "+ride.getId()));

        if(ratingObj.getRiderRating() != null )throw new RuntimeConflictExceptions("RIDER HAS ALREADY BEEN RATED");
        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getRiderRating)
                .average()
                .orElse(0.0);
        rider.setRating(newRating);
        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);



    }

    @Override
    public void createNewRating(Ride ride) {
       Rating rating = Rating
               .builder()
               .rider(ride.getRider())
               .driver(ride.getDriver())
               .ride(ride)
               .build();
       ratingRepository.save(rating);
    }









}
