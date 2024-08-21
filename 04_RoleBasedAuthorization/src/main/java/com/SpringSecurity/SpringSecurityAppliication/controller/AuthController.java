package com.SpringSecurity.SpringSecurityAppliication.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurity.SpringSecurityAppliication.dto.LoginDto;
import com.SpringSecurity.SpringSecurityAppliication.dto.LoginResponseDto;
import com.SpringSecurity.SpringSecurityAppliication.dto.SignupDto;
import com.SpringSecurity.SpringSecurityAppliication.dto.UserDto;
import com.SpringSecurity.SpringSecurityAppliication.services.AuthService;
import com.SpringSecurity.SpringSecurityAppliication.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @Value("${deploy.env}")
  private String DeployeEnv;

  @PostMapping(path = "/signup")
  public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
    UserDto userDto = userService.signup(signupDto);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
      HttpServletResponse response) {

    LoginResponseDto loginResponseDto = authService.login(loginDto);

    Cookie cookie = new Cookie("refreshToken",loginResponseDto.getRefreshToken());

    cookie.setHttpOnly(true);
    cookie.setSecure("production".equals(DeployeEnv));  // means is this environment variable is production inside the application.properties only when use setSecure bcz we don;t want to use setSecure true  in development
    response.addCookie(cookie);

    return ResponseEntity.ok(loginResponseDto);
  }


  @PostMapping(path = "/refresh")
  public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){
   String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie->"refreshToken" .equals(cookie.getName()))
                 .findFirst()
                 .map(cookie->cookie.getValue())
                 .orElseThrow(()-> new AuthenticationServiceException("refresh token not found inside the cookies"));
                 LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
                 return ResponseEntity.ok(loginResponseDto);
  }


}
