package com.example.FoodExpress.service;
import com.example.FoodExpress.entity.UserEntity;
import com.example.FoodExpress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
