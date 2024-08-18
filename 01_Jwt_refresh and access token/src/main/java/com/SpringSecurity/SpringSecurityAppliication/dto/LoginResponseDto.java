package com.SpringSecurity.SpringSecurityAppliication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private long id;
    private String accessToken;
    private String refreshToken;
}
