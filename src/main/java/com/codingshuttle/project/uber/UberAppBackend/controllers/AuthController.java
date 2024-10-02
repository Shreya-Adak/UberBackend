package com.codingshuttle.project.uber.UberAppBackend.controllers;

import com.codingshuttle.project.uber.UberAppBackend.dtos.*;
import com.codingshuttle.project.uber.UberAppBackend.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

//@RestController
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signupDto) {

        return new ResponseEntity<>(authService.signUp(signupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId,
                                                      @RequestBody OnBoardDriverDto onBoardDriverDto){
        return new ResponseEntity<>(authService.onboardNewDriver(userId,onBoardDriverDto.getVehicleId()),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response){
        String[] tokens = authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

        Cookie cookie = new Cookie("token",tokens[1]);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));

    }

    @PostMapping("/refresh")  //6.1vdo
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){
        String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("Refresh Token not found inside the cookies: "));
        String accessToken =authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }

}