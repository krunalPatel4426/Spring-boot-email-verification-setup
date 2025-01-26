package com.Ecommerce.e_commerce.repo;

import com.Ecommerce.e_commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
