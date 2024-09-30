package com.codingshuttle.project.uber.UberAppBackend.dtos;

import lombok.Data;

@Data
public class RateDto {
    private Long rideId;
    private Integer rating;
}
