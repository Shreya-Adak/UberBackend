package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.RideRequestRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImplementation implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository
                .findById(rideRequestId)
                .orElseThrow(()-> new ResourceNotFoundExceptions("RideRequest not found with id: "+rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {

        rideRequestRepository
                .findById(rideRequest.getId())
                .orElseThrow(()->
                        new ResourceNotFoundExceptions("RideRequest not found with id: "+rideRequest.getId()));

        rideRequestRepository.save(rideRequest);
    }
}
