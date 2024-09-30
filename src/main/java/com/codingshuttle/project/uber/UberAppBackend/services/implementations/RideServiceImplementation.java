package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideRequestStatus;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideStatus;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RideRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.RideRequestService;
import com.codingshuttle.project.uber.UberAppBackend.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImplementation implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository
                .findById(rideId)
                .orElseThrow(()->new ResourceNotFoundExceptions("RIDE IS NOT FOUND WITH ID: "+rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);

    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    public String generateOTP(){
        Random random = new Random();
        int otp = random.nextInt(10000); // 0 to 9999

        return String.format("%04d",otp);

    }


}
