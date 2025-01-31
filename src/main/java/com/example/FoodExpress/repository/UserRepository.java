package com.example.FoodExpress.repository;

import com.example.FoodExpress.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
        UserEntity findByUsernameAndPassword(String username, String password);
        UserEntity findByEmail(String email);
        UserEntity findByUsername(String username);

}

