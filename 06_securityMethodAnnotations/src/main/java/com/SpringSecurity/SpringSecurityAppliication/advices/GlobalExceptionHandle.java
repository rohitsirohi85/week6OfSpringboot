package com.SpringSecurity.SpringSecurityAppliication.advices;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.SpringSecurity.SpringSecurityAppliication.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandle {
    
         @ExceptionHandler(ResourceNotFoundException.class)
         
        public ResponseEntity<ApiError> handlingResourceNotFoundException(ResourceNotFoundException exception){
           ApiError apiError =new ApiError(exception.getLocalizedMessage() , HttpStatus.NOT_FOUND);
           return new ResponseEntity<>(apiError , HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiError> handlingAuthenticationException(AuthenticationException exception){
         ApiError apiError = new ApiError(exception.getLocalizedMessage() , HttpStatus.UNAUTHORIZED);
         return new ResponseEntity<>(apiError , HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(JwtException.class)
        public ResponseEntity<ApiError> handlingJwtException(JwtException exception){
         ApiError apiError = new ApiError(exception.getLocalizedMessage() , HttpStatus.UNAUTHORIZED);
         return new ResponseEntity<>(apiError , HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiError> handlingAccessDeniedException(AccessDeniedException exception){
         ApiError apiError = new ApiError(exception.getLocalizedMessage() , HttpStatus.FORBIDDEN);
         return new ResponseEntity<>(apiError , HttpStatus.FORBIDDEN);
        }
}
