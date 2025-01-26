package com.Ecommerce.e_commerce.service;

import com.Ecommerce.e_commerce.model.User;
import com.Ecommerce.e_commerce.model.VerificationToken;
import com.Ecommerce.e_commerce.repo.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private VerificationTokenRepo tokenRepo;

    public String createVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); //24 hours expiry
        tokenRepo.save(verificationToken);
        return token;
    }

}
