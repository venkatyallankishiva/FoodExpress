package registerationlogin.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import registerationlogin.entity.Restaurant;
import registerationlogin.service.RestaurantService;

@Controller
@RequestMapping("/restaurant/reports")
public class ReportViewController {

    private RestaurantService restaurantService;

    public ReportViewController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/sales")
    public String showSalesReport(Model model, Principal principal) {
        Long restaurantId = getRestaurantId(principal);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        if (restaurant == null) {
            model.addAttribute("error", "Restaurant not found!");
            model.addAttribute("Page", "orders"); // Flag to load Sales Report in content
            return "restaurant-template1";
        }

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("Page", "sales"); // Flag to load Sales Report in content
        return "restaurant-template1";
    }

    @GetMapping("/customers")
    public String showCustomerInsightsReport(Model model, Principal principal) {

        Long restaurantId = getRestaurantId(principal);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        if (restaurant == null) {
            model.addAttribute("error", "Restaurant not found!");
            model.addAttribute("Page", "customers"); // Flag to load Customer Insights Report
            return "restaurant-template1";
        }

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("Page", "customers"); // Flag to load Customer Insights Report
        return "restaurant-template1";
    }

    public Long getRestaurantId(Principal principal) {
        // String email = principal.getName();
        String email = getAuthenticatedUserEmail();

        Restaurant restaurant = restaurantService.getRestaurantByEmail(email);
        return restaurant.getId();
    }

      private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            // Normal login (email/password)
            return authentication.getName();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            // Google login
            OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();

            // 🔴 DEBUG: Print all attributes to verify the email key
            System.out.println("OAuth2 Attributes: " + oauthUser.getAttributes());

            return (String) oauthUser.getAttributes().get("email"); // Extract email
        }

        return null; // If no valid authentication is found
    }

    

}
