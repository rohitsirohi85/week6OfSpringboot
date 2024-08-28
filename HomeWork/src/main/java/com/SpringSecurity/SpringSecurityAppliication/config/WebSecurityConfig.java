package com.SpringSecurity.SpringSecurityAppliication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role;
import com.SpringSecurity.SpringSecurityAppliication.Handlers.OAuth2SuccessHandler;
import com.SpringSecurity.SpringSecurityAppliication.filters.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // used for using web security
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true) // use for using security method annotations
public class WebSecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;
        private final OAuth2SuccessHandler oAuth2SuccessHandler;

        private static final String[] publicRoutes = {
                        "/error", "/auth/**", "/home.html"
        };

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(publicRoutes).permitAll()
                                                .requestMatchers("/posts/**").authenticated()

                                                .anyRequest().authenticated())
                                .csrf(csrfConfig -> csrfConfig.disable())
                                .sessionManagement(sessionConfig -> sessionConfig
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                
                                .oauth2Login(oauth2Config -> oauth2Config
                                                .failureUrl("/login?error=true")
                                                .successHandler(oAuth2SuccessHandler));
                             
                     

                return httpSecurity.build();
        }

        @Bean
        AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        // @Bean
        // UserDetailsService MyInMemoryUserDetails(){
        // UserDetails
        // normalUser=User.withUsername("rohit").password(passwordEncoder().encode("rohit1212")).roles("USER").build();

        // UserDetails
        // adminUser=User.withUsername("sirohi").password(passwordEncoder().encode("pass")).roles("ADMIN").build();

        // return new InMemoryUserDetailsManager(normalUser,adminUser);
        // }

}
