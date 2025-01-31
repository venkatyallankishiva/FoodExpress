package com.example.FoodExpress.Controller;

import com.example.FoodExpress.entity.Restaurant;
import com.example.FoodExpress.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

}
