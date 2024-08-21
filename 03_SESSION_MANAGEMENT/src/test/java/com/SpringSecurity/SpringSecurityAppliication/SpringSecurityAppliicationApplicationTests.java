package com.SpringSecurity.SpringSecurityAppliication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.services.JwtService;

@SpringBootTest
class SpringSecurityAppliicationApplicationTests {

      @Autowired
	  private JwtService jwtService;


	@Test
	void contextLoads() {
		// User user= new User(4L , "rohit@gmail.com" , "1234" , "rohit");

		// String token = jwtService.createTokens(user);

		// System.out.println(token);

		// Long id=jwtService.getUserIdFromToken(token);

		// System.out.println(id);

	}

}
