package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.dtos.DriverDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.SignUpDto;
import com.codingshuttle.project.uber.UberAppBackend.dtos.UserDto;
import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.Role;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.RuntimeConflictExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.UserRepository;
import com.codingshuttle.project.uber.UberAppBackend.security.JWTService;
import com.codingshuttle.project.uber.UberAppBackend.services.AuthService;
import com.codingshuttle.project.uber.UberAppBackend.services.DriverService;
import com.codingshuttle.project.uber.UberAppBackend.services.RiderService;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.codingshuttle.project.uber.UberAppBackend.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
// to achieve the scalable, readability and loosely coupling we made interfaces of services first and then implement this services
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public String[] login(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email,password)
        );
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken,refreshToken};
    }

    @Override
    @Transactional
    public UserDto signUp(SignUpDto signUpDto) {

        User user = userRepository.findByEmail(signUpDto.getEmail()).orElse(null);
        if(user != null)
            throw new RuntimeConflictExceptions("Cannot signup, User already exists with email "+signUpDto.getEmail());
        User mappedUser = modelMapper.map(signUpDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        User savedUsers = userRepository.save(mappedUser);

        //create user related entities
        riderService.createRider(savedUsers);

        walletService.createNewWallet(savedUsers);

        return modelMapper.map(savedUsers,UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(()->new ResourceNotFoundExceptions("USER NOT FOUND WITH USER ID: "+userId));
        if(user.getRoles().contains(DRIVER))
            throw new RuntimeConflictExceptions("USER WITH ID "+userId+" IS ALREADY A DRIVER");

        Driver createDriver = Driver
                .builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        user.getRoles().add(DRIVER);
        userRepository.save(user);

        Driver savedDriver = driverService.createNewDriver(createDriver);

        return modelMapper.map(savedDriver,DriverDto.class);


    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);


        User user = userRepository
                .findById(userId)
                .orElseThrow(()->new ResourceNotFoundExceptions("USER NOT FOUND WITH ID: "+userId));

        String accessToken = jwtService.generateAccessToken(user);

        return accessToken;
    }


}
