package com.example.FoodExpress.Controller;

import com.example.FoodExpress.service.RestaurantService;
import com.example.FoodExpress.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RestaurantService restaurantService;

    // Welcome page mapping
    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {
        // Assuming there's a logged-in user, add the user to the model
        model.addAttribute("user", "Anil");
        return "welcome";
    }

    // Order page mapping
    @GetMapping("/order")
    public String showRestaurantList(Model model) {
        // Add the user to the model
        model.addAttribute("user", "koushik");

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "restaurant-list";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }
    @GetMapping("/restaurant/{id}")
    public String viewMenu(@PathVariable("id") Long restaurantId, Model model) {
        model.addAttribute("restaurant", restaurantService.findRestaurantById(restaurantId));
        model.addAttribute("menuItems", restaurantService.getMenuItemsByRestaurantId(restaurantId));
        return "restaurant";
    }
}
