package com.codingshuttle.project.uber.UberAppBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderDto {
    private  Long id;
    private UserDto user;
    private Double rating;
}
