package registerationlogin.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import registerationlogin.entity.Restaurant;
import registerationlogin.service.DashboardService;
import registerationlogin.service.RestaurantService;
import registerationlogin.service.impl.DashboardServiceImpl;

@RestController
@RequestMapping("/restaurant/dashboard")
public class DashboardController {

    private DashboardService dashboardService;
    private RestaurantService restaurantService;


    public DashboardController(DashboardServiceImpl dashboardService , RestaurantService restaurantService) {
        this.dashboardService = dashboardService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/sales")
    public DashboardServiceImpl.SalesStats getSalesStats(Principal principal) {
        Long restaurantId = getRestaurantId(principal);
        System.out.println(restaurantId);

        // display the sales statt

        System.out.println(dashboardService.calculateTodaySalesAndPercentageChange(restaurantId));

        return dashboardService.calculateTodaySalesAndPercentageChange(restaurantId);
    }

    // @GetMapping("/sales")
    // public Map<String, Object> getSalesData() {
    //     return dashboardService.getSalesData();
    // }

    // @GetMapping("/orders")
    // public Map<String, Object> getOrdersData() {
    //     return dashboardService.getOrdersData();
    // }

    // @GetMapping("/tables")
    // public Map<String, Object> getTablesOccupied() {
    //     return dashboardService.getTablesData();
    // }

    // @GetMapping("/customer-satisfaction")
    // public Map<String, Object> getCustomerSatisfaction() {
    //     return dashboardService.getCustomerSatisfaction();
    // }

    // @GetMapping("/sales-trend")
    // public Map<String, Object> getSalesTrend() {
    //     return dashboardService.getSalesTrend();
    // }

    // @GetMapping("/top-items")
    // public Map<String, Object> getTopSellingItems() {
    //     return dashboardService.getTopSellingItems();
    // }

       public Long getRestaurantId(Principal principal) {
        String email = getAuthenticatedUserEmail();
        // String email = principal.getName();
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
