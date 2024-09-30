package com.codingshuttle.project.uber.UberAppBackend.dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
