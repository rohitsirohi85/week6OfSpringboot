package com.SpringSecurity.SpringSecurityAppliication.services;



import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import com.SpringSecurity.SpringSecurityAppliication.Entity.Session;
import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.SubscriptionPlans;
import com.SpringSecurity.SpringSecurityAppliication.exceptions.ResourceNotFoundException;
import com.SpringSecurity.SpringSecurityAppliication.repo.SessionRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepo sessionRepo;

    public void generateSession(User user , String refreshToken){

        // make the logic to maintain the session limit by subscription plan
        SubscriptionPlans subscriptionPlans = user.getSubscriptionPlans();
         int SESSION_LIMIT=0;  // if user has no plan 
          
         if (subscriptionPlans.equals(SubscriptionPlans.FREE)) {
           SESSION_LIMIT=1;
         }else if (subscriptionPlans.equals(SubscriptionPlans.BASIC)) {
               SESSION_LIMIT=2;
         }else{
          SESSION_LIMIT=3;
         }

        List<Session> userSessions = sessionRepo.findByUser(user);
      if (userSessions.size()==SESSION_LIMIT) {
           userSessions.sort(
             Comparator.comparing(Session::getLastUsedAt)
           );
           Session leastRecentlyUsedSession = userSessions.getFirst();
           sessionRepo.delete(leastRecentlyUsedSession);
      }

      // if session limit not exceeded
      Session newSession = Session.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .build();
                    sessionRepo.save(newSession);
    }

    public void validate(String refreshToken){
       Session session =  sessionRepo.findByRefreshToken(refreshToken).orElseThrow(()->new SessionAuthenticationException("Session not found with this refresh token"));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepo.save(session);
    }
    public void removeSession(String refreshToken) {
      Session session = sessionRepo.findByRefreshToken(refreshToken)
              .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
      sessionRepo.deleteById(session.getId());
    }
}
