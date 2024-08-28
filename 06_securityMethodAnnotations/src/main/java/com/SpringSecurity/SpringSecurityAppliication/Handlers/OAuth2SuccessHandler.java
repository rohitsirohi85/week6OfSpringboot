package com.SpringSecurity.SpringSecurityAppliication.Handlers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.services.JwtService;
import com.SpringSecurity.SpringSecurityAppliication.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

 
  private final UserService userService;
  private final JwtService jwtService;

  @Value("${deploy.env}")
  private String deployEnv;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

      OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
      DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

      String email = oAuth2User.getAttribute("email");

      User user = userService.getUserByEmail(email);

      if(user == null) {
          User newUser = User.builder()
                  .name(oAuth2User.getAttribute("name"))
                  .email(email)
                  .build();
          user = userService.save(newUser);
      }

      String accessToken = jwtService.createAccessTokens(user);
      String refreshToken = jwtService.createRefreshTokens(user);

      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setSecure("production".equals(deployEnv));
      response.addCookie(cookie);

      String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

      response.sendRedirect(frontEndUrl);
  }

}