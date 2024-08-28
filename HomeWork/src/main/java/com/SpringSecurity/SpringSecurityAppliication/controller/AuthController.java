package com.SpringSecurity.SpringSecurityAppliication.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

     // replace the new refresh token with old from the cookie every time using /login so no one can use old token
    Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());

    cookie.setHttpOnly(true);
    cookie.setSecure("production".equals(DeployeEnv)); // means is this environment variable is production inside the
                                                       // application.properties only when use setSecure bcz we don;t
        cookie.setPath("/auth");                                               // want to use setSecure true in development
    response.addCookie(cookie);

    return ResponseEntity.ok(loginResponseDto);
  }

  @PostMapping(path = "/logout")
public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

  // Start by retrieving the "refreshToken" from the cookies in the HTTP request
String refreshToken = Arrays.stream(request.getCookies())  // Convert the array of cookies into a Stream for easy processing

    .filter(cookie -> "refreshToken".equals(cookie.getName()))  // Filter to find the cookie named "refreshToken"

    .findFirst()  // Retrieve the first matching cookie (if any) as an Optional<Cookie>

    .map(Cookie::getValue)  // Extract the value of the cookie, converting Optional<Cookie> to Optional<String>

    .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the cookies"));  
    // If the Optional is empty (no matching cookie found), throw an exception


    // Perform logout logic (e.g., invalidate token in database)
    authService.logout(refreshToken);

// Create a new cookie with the same name "refreshToken" but an empty value to overwrite the existing one
Cookie refreshTokenCookie = new Cookie("refreshToken", "");

// Set the path to match the original cookie's path (this must be the same as when the cookie was originally set)
refreshTokenCookie.setPath("/auth");  // Ensure this path matches the original one

// Mark the cookie as HttpOnly to prevent client-side scripts from accessing it
refreshTokenCookie.setHttpOnly(true);

// If the original cookie was set with the Secure flag, ensure the new cookie is also marked Secure
refreshTokenCookie.setSecure(true);  // Ensure consistency with the original cookie's security settings

// Set the MaxAge to 0 to immediately expire the cookie
refreshTokenCookie.setMaxAge(0);  // This tells the browser to delete the cookie immediately

// Add the newly configured cookie to the response, effectively removing the original cookie
response.addCookie(refreshTokenCookie);


    return ResponseEntity.noContent().build(); // Return 204 No Content status
}


  @PostMapping(path = "/refresh")
  public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request , HttpServletResponse response) {
    String refreshToken = Arrays.stream(request.getCookies())
        .filter(cookie -> "refreshToken".equals(cookie.getName()))
        .findFirst()
        .map(cookie -> cookie.getValue())
        .orElseThrow(() -> new AuthenticationServiceException("refresh token not found inside the cookies"));
    LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

    // replace the new refresh token with old from the cookie every time using /refresh so no one can use old token
    Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());

    cookie.setHttpOnly(true);
    cookie.setSecure("production".equals(DeployeEnv)); // means is this environment variable is production inside the
                                                       // application.properties only when use setSecure bcz we don;t
        cookie.setPath("/auth");                                               // want to use setSecure true in development
    response.addCookie(cookie);

    return ResponseEntity.ok(loginResponseDto);
  }

}
