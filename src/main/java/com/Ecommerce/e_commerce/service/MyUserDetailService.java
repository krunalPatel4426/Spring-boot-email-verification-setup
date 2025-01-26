package com.Ecommerce.e_commerce.service;

import com.Ecommerce.e_commerce.model.User;
import com.Ecommerce.e_commerce.model.UserPrincipal;
import com.Ecommerce.e_commerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User Not Found.");
        }
        return new UserPrincipal(user);
    }
}
