package com.example.FoodExpress.service;

import com.example.FoodExpress.entity.MenuItem;
import com.example.FoodExpress.entity.Restaurant;
import com.example.FoodExpress.repository.RestaurantRepository;
import com.example.FoodExpress.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }


public void saveRestaurant(Restaurant restaurant) {
    restaurantRepository.save(restaurant);
}


    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
