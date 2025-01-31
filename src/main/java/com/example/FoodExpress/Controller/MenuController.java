package com.example.FoodExpress.Controller;

import com.example.FoodExpress.service.MenuItemService;
import com.example.FoodExpress.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MenuController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/add-menu")
    public String addMenuItemForm() {
        return "add-menu";
    }}

