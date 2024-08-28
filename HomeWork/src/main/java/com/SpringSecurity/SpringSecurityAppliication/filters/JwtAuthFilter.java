package com.SpringSecurity.SpringSecurityAppliication.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.services.JwtService;
import com.SpringSecurity.SpringSecurityAppliication.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component   // bcz we have to make it a bean can be used
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
     private final UserService userService;


     @Autowired
     @Qualifier("handlerExceptionResolver")
     private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

              try {
                final String requestTokenHeader = request.getHeader("Authorization");
                if (requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer")) {
                 filterChain.doFilter(request, response);
                 return;
                }
         
                String token = requestTokenHeader.split("Bearer ")[1]; // the space after Bearer is important bcz it will separate the string to token without space
               Long userId = jwtService.getUserIdFromToken(token);  //get the userId token with the method we create in jwtService
         
               // now use this userId to access User data
         
               if (userId != null & SecurityContextHolder.getContext().getAuthentication()==null) {
                 User user = userService.getUserById(userId);
                 UsernamePasswordAuthenticationToken authenticationToken =
                           new UsernamePasswordAuthenticationToken(user , null , user.getAuthorities());
                           authenticationToken.setDetails(

                             new WebAuthenticationDetailsSource().buildDetails(request)
                           );
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
               }
               filterChain.doFilter(request, response);
         
              } catch (Exception e) {
                handlerExceptionResolver.resolveException(request, response, null, e);
              }
      
    }
    
}
