package com.example.FoodExpress.service;

import com.example.FoodExpress.entity.Admin;
import com.example.FoodExpress.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin registerAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    public Admin validateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}

