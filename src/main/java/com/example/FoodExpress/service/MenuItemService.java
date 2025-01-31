package com.example.FoodExpress.service;

import com.example.FoodExpress.entity.MenuItem;
import com.example.FoodExpress.entity.Restaurant;
import com.example.FoodExpress.repository.MenuItemRepository;
import com.example.FoodExpress.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public void addMenuItem(String name, String description, Long restaurantId, double price, String imageUrl) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
        MenuItem menuItem = new MenuItem(null, name, price, description, imageUrl, restaurant);
        menuItemRepository.save(menuItem);
    }
    public void saveMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}

