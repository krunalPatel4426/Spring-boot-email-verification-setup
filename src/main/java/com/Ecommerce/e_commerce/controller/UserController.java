package com.Ecommerce.e_commerce.controller;

import com.Ecommerce.e_commerce.model.User;
import com.Ecommerce.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam String token){
        return userService.verifyToken(token);
    }
}
