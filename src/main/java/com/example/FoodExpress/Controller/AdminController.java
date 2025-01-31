package com.example.FoodExpress.Controller;

import com.example.FoodExpress.entity.Admin;
import com.example.FoodExpress.entity.MenuItem;
import com.example.FoodExpress.entity.Restaurant;
import com.example.FoodExpress.service.AdminService;
import com.example.FoodExpress.service.MenuItemService;
import com.example.FoodExpress.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/admin/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "adminRegistration";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute Admin admin, Model model) {
        Admin existingUser = adminService.findByEmail(admin.getEmail());
        if (existingUser != null) {
            model.addAttribute("error", "An account with that email already exists.");
            return "adminRegistration";
        }
        adminService.registerAdmin(admin);
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/login")
    public String showLoginForm() {
        return "adminLogin";
    }

    @PostMapping("/admin/login")
    public String loginAdmin(@RequestParam String username, @RequestParam String password, Model model) {
        Admin admin = adminService.validateAdmin(username, password);
        if (admin != null) {
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "adminLogin";
        }
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "adminDashboard";
    }

    @GetMapping("/admin/addRestaurant")
    public String showAddRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "addRestaurant";
    }

    @PostMapping("/admin/addRestaurant")
    public String addRestaurant(@ModelAttribute Restaurant restaurant) {
        restaurantService.saveRestaurant(restaurant);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/restaurant/{id}/menu")
    public String viewMenu(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItems", menuItemService.getMenuItemsByRestaurantId(id));
        return "viewMenu";
    }

    @GetMapping("/restaurant/{id}/addMenuItem")
    public String showAddMenuItemForm(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurant(restaurant);
        model.addAttribute("menuItem", menuItem);
        return "add-menu";
    }

    @PostMapping("/add-menu-item")
    public String addMenuItem(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("restaurantId") Long restaurantId,
                              @RequestParam("price") double price,
                              @RequestParam("imageUrl") String imageUrl,
                              Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setRestaurant(restaurant);
        menuItem.setPrice(price);
        menuItem.setImageUrl(imageUrl);
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/restaurant/" + restaurantId + "/menu";
    }
}
