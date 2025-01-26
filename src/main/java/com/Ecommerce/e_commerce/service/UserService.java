package com.Ecommerce.e_commerce.service;

import com.Ecommerce.e_commerce.DTO.RegistrationDTO;
import com.Ecommerce.e_commerce.model.User;
import com.Ecommerce.e_commerce.model.VerificationToken;
import com.Ecommerce.e_commerce.repo.UserRepo;
import com.Ecommerce.e_commerce.repo.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private VerificationTokenRepo tokenRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager manager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<Object> register(User user){
       try{
           user.setEmailVerified(false);
           user.setPassword(encoder.encode(user.getPassword()));
           repo.save(user);

           String token = tokenService.createVerificationToken(user);

           String verificationLink = "http://localhost:5500/verify.html?token=" + token;

           emailService.sendVerificationEmail(user.getEmail(), verificationLink);

           return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationDTO("User Registred successfully.", "CREATED"));
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationDTO("User Already Exists.", "BAD_REQUEST"));
       }
    }

    public ResponseEntity<Object> verifyToken(String token){
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        if(verificationToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RegistrationDTO("Invalid Token.", "UNAUTHORIZED"));
        }
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RegistrationDTO("Token Expired.", "UNAUTHORIZED"));
        }
        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        repo.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new RegistrationDTO("Email Verified.", "ACCEPTED"));
    }

    public String verify(User user){
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }else{
            return "Failed";
        }
    }
}
