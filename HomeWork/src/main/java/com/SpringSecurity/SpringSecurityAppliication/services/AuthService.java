package com.SpringSecurity.SpringSecurityAppliication.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.dto.LoginDto;
import com.SpringSecurity.SpringSecurityAppliication.dto.LoginResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
             private final AuthenticationManager authenticationManager;  // only used for authentication
         private final JwtService jwtService;
         private final UserService userService;
         private final SessionService sessionService;

          public LoginResponseDto login(LoginDto loginDto) {
    Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
      );


      // it will create and return token if login success

      User user = (User) authentication.getPrincipal();
      String accessToken = jwtService.createAccessTokens(user);
      String refreshToken = jwtService.createRefreshTokens(user);
       sessionService.generateSession(user, refreshToken);
       return new LoginResponseDto(user.getId() , accessToken , refreshToken);
   }

         public LoginResponseDto refreshToken(String refreshToken) {
            // find user from token
          Long userId = jwtService.getUserIdFromToken(refreshToken);

          // validate session from token
          sessionService.validate(refreshToken);
          User user = userService.getUserById(userId);

          //create fresh tokens when every time hit /refresh
          String newAccessToken = jwtService.createAccessTokens(user);
          String newRefreshToken = jwtService.createRefreshTokens(user);
          
          // remove session associate with that token and create a new session
          
          sessionService.removeSession(refreshToken);
          sessionService.generateSession(user, newRefreshToken);
          
          return new LoginResponseDto(user.getId() ,newAccessToken , newRefreshToken);

         }

        public void logout(String refreshToken) {
          sessionService.removeSession(refreshToken);
        }

}
