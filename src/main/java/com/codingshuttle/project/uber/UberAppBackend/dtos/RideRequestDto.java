package com.codingshuttle.project.uber.UberAppBackend.dtos;

import com.codingshuttle.project.uber.UberAppBackend.entities.enums.PaymentMethod;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long id;

    private PointDto pickUpLocation;

    private PointDto dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;

    private Long fare;

}
