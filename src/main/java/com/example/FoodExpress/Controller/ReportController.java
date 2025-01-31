package com.example.FoodExpress.Controller;


import com.example.FoodExpress.entity.MenuItem;
import com.example.FoodExpress.repository.CustomerRepository;
import com.example.FoodExpress.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/report")
    public String viewReportPage(Model model) {
        return "report"; // Return the report page template
    }

    @PostMapping("/generateReport")
    public String generateReport(@RequestParam("menuReport") String menuReport,
                                 @RequestParam("orderReport") String orderReport,
                                 @RequestParam("customerActivity") String customerActivity,
                                 @RequestParam("menuPreferences") String menuPreferences,
                                 Model model) {
        long totalCustomers = customerRepository.count();


        List<MenuItem> popularMenuItems = menuItemRepository.findAll();

        // Add data to the model
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("popularMenuItems", popularMenuItems);
        model.addAttribute("reportGenerated", true);

        return "report"; // Return the same page with report data
    }
}
