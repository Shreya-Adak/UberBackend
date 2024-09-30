package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RideDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.RiderDto;
import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideRequestStatus;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideStatus;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.DriverRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService {


    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {

        RideRequest rideRequest = rideRequestService
                .findRideRequestById(rideRequestId);

        if(!rideRequest
                .getRideRequestStatus()
                .equals(RideRequestStatus.PENDING)){
            throw new RuntimeException(
                    "RIDE REQUEST CAN NOT BE ACCEPTED: "
                            +rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
          throw new RuntimeException(
                  "DRIVER CAN NOT ACCEPT RIDE DUE TO UNAVAILABILITY");
        }

//        currentDriver.setAvailable(false);
//        Driver savedDriver = driverRepository.save(currentDriver);
        Driver savedDriver = updateDriverAvailability(
                currentDriver,false);
        Ride ride = rideService.createNewRide(rideRequest,savedDriver);
       // Ride ride = rideService.createNewRide(rideRequest,currentDriver);

        return modelMapper.map(ride, RideDto.class);
    }



    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);

        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("DRIVER CAN NOT START A RIDE AS HE HAS NOT ACCEPTED IT EARLIER");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("RIDE CAN NOT BE CANCELLED, INVALID STATUS : "+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
//        driver.setAvailable(true);
//        driverRepository.save(driver);

        updateDriverAvailability(driver,true);

        return modelMapper.map(ride,RideDto.class);

    }

    @Override
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("DRIVER CAN NOT START A RIDE AS HE HAS NOT ACCEPTED IT EARLIER");
        }

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("RIDE STATUS IS NOT CONFIRMED HENCE CAN NOT BE STARTED, STATUS: "+ ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("OTP IS NOT VALID, otp: "+otp);
        }

        ride.setStartedAt(LocalDateTime.now());

        Ride savedRide = rideService
                .updateRideStatus(ride,RideStatus.ONGOING);


        paymentService.createPayment(savedRide);
        ratingService.createNewRating(savedRide);


        return modelMapper.map(savedRide, RideDto.class);


    }



    @Override
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("DRIVER CAN NOT START A RIDE AS HE HAS NOT ACCEPTED IT EARLIER");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("RIDE STATUS IS NOT ONGOING HENCE CAN NOT BE ENDED, STATUS: "+ ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide,RideDto.class);



    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("DRIVER IS NOT THE OWNER OF THIS RIDE");
        }

        if (!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("RIDE STATUS IS NOT ENDED HENCE CAN NOT BE START RATING : "+ ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);

    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver,DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();

        return rideService.getAllRidesOfDriver(currentDriver,pageRequest).map(
                        ride -> modelMapper.map(ride, RideDto.class)

        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        return driverRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundExceptions(
                        "Diver not associated with user with id: " + user.getId()
                ));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean availability) {

        driver.setAvailable(availability);
        return driverRepository.save(driver);

    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
