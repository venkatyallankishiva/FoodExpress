package com.example.FoodExpress.Controller;

import com.example.FoodExpress.entity.UserEntity;
import com.example.FoodExpress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MyController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showWelcomePage() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @GetMapping("/register")
    public String showRegisterPage() {
        return "Register";
    }

    @PostMapping("/register")
    public String registerUser(UserEntity user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
    @PostMapping("/login")
    public String loginUser(String username, String password, Model model) {
        UserEntity user = userService.findByUsernameAndPassword(username, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "welcome";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }
}


