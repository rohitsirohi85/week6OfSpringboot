package com.SpringSecurity.SpringSecurityAppliication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.SpringSecurity.SpringSecurityAppliication.filters.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
      
        .authorizeHttpRequests(auth->auth
        .requestMatchers("/posts" , "/auth/**").permitAll() // so it endpoint can be accessible by public no need to use password here 
       // .requestMatchers("/posts/**").hasAnyRole("ADMIN") // so it endpoint can be accessible by admin 
        
        .anyRequest().authenticated()) // all other request has to be authenticated
        .csrf(csrfConfig->csrfConfig.disable()) //it will disable the csrf login token and if we don't use login page so we don't need it csrf token either right
        .sessionManagement(sessionconfig-> sessionconfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // we will use JWT token for these 
        .addFilterBefore( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
           
        // .formLogin(Customizer.withDefaults()); // this will create a html login form by default but we don't need it on backend bcz it's a duty of frontend to create login page for us



        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    // @Bean
    // UserDetailsService MyInMemoryUserDetails(){
    //     UserDetails normalUser=User.withUsername("rohit").password(passwordEncoder().encode("rohit1212")).roles("USER").build();

    //     UserDetails adminUser=User.withUsername("sirohi").password(passwordEncoder().encode("pass")).roles("ADMIN").build();

    //     return new InMemoryUserDetailsManager(normalUser,adminUser);
    // }

  

}
