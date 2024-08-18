package com.SpringSecurity.SpringSecurityAppliication.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.dto.SignupDto;
import com.SpringSecurity.SpringSecurityAppliication.dto.UserDto;
import com.SpringSecurity.SpringSecurityAppliication.exceptions.ResourceNotFoundException;
import com.SpringSecurity.SpringSecurityAppliication.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

         private final UserRepo userRepo;
         private final ModelMapper modelMapper;
         private final PasswordEncoder passwordEncoder;
        
       

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepo.findByEmail(username).orElseThrow(()->
                     new BadCredentialsException("user with email "+username+" not found")
       );
    }

    public User getUserById(Long userId){
      return userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user with id "+userId+" not found"));
    }

      // implementing signup api 
    public UserDto signup(SignupDto signupDto) {
      Optional<User> user = userRepo.findByEmail(signupDto.getEmail());
      if (user.isPresent()) {
         throw new BadCredentialsException("user with email already exist : " + signupDto.getEmail());  
      }

      User toBeCreated = modelMapper.map(signupDto, User.class);
      toBeCreated.setPassword(passwordEncoder.encode(signupDto.getPassword()));
      User savedUser = userRepo.save(toBeCreated);
      return modelMapper.map(savedUser, UserDto.class);
    }


    
}
