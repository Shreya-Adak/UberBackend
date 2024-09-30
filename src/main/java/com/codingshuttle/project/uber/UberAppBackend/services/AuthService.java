package com.codingshuttle.project.uber.UberAppBackend.services;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.SignUpDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.UserDto;

public interface AuthService {
    String[] login(String email, String password);
    UserDto signUp(SignUpDto signUpDto);
    DriverDto onboardNewDriver(Long userId,String vehicleId);

    String refreshToken(String refreshToken);
}
