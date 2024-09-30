package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RideDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RideRequestDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RiderDto;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideRequestStatus;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideStatus;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.DriverRepository;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RideRequestRepository;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RiderRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.DriverService;
import com.codingshuttle.project.uber.UberAppBackend.services.RatingService;
import com.codingshuttle.project.uber.UberAppBackend.services.RideService;
import com.codingshuttle.project.uber.UberAppBackend.services.RiderService;
import com.codingshuttle.project.uber.UberAppBackend.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImplementation implements RiderService {
    private final DriverRepository driverRepository;


    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
//    private final RideFareCalculationStrategy rideFareCalculationStrategy;
//    private final DriverMatchingStrategy driverMatchingStrategy;

    //or we can use

    private final RideStrategyManager rideStrategyManager;

    private final RideRequestRepository rideRequestRepository;

    private final RideService rideService;

    private final DriverService driverService;


    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        // Dummy Data for testing
        Rider rider = getCurrentRider();

        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);

        // log.info(rideRequest.toString());

        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);

        //check log statement
        log.info("Calculated Fare: {}", fare);
        rideRequest.setFare(fare);


        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }
//        Rider rider = getCurrentRider();
//
//        RideRequest rideRequest = modelMapper.map(
//                rideRequestDto,RideRequest.class);
//
//        rideRequest.setRideRequestStatus(
//                RideRequestStatus.PENDING);
//
//        rideRequest.setRider(rider);
//
//    //    Double fare = rideFareCalculationStrategy.calculateFare(toSavedRideRequest);
//        // or
//        Double fare = rideStrategyManager
//                .rideFareCalculationStrategy()
//                .calculateFare(rideRequest);
//        rideRequest.setFare(fare);
//
//        RideRequest savedRideRequest = rideRequestRepository
//                .save(rideRequest);
//
//      // driverMatchingStrategy.findMatchingDriver(toSavedRideRequest);
//        //or
////        rideStrategyManager
////                .driverMatchingStrategy(rider.getRating())
////                .findMatchingDriver(toSavedRideRequest);
//
//        List<Driver> drivers = rideStrategyManager
//                .driverMatchingStrategy
//                        (rider.getRating())
//                .findMatchingDriver(rideRequest);
//    // TODO :   Send notification to all the drivers about this ride request
//
//        return modelMapper.map(savedRideRequest,RideRequestDto.class);

  //  }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();

        Ride ride = rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("RIDER DOES NOT CAN THIS RIDE WITH ID: "+rideId);
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("RIDE CAN NOT BE CANCELLED, INVALID STATUS : "+ride.getRideStatus());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(),true);


        return modelMapper.map(savedRide,RideDto.class);

    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("RIDER IS NOT THE OWNER OF THIS RIDE");
        }

        if (!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("RIDE STATUS IS NOT ENDED HENCE CAN NOT BE START RATING : "+ ride.getRideStatus());
        }

        return ratingService.rateDriver(ride,rating);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider,RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider,pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );

    }

    @Override
    public Rider createRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        return riderRepository
                .findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundExceptions(
                "Rider not associated with user with id: " +user.getId()
        ));
    }
}
