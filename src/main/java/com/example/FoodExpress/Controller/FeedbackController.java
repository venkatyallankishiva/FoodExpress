package com.example.FoodExpress.Controller;

import com.example.FoodExpress.entity.Feedback;
import com.example.FoodExpress.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/feedback")
    public String feedbackForm(Model model) {
        return "feedback";
    }

    @PostMapping("/submit_feedback")
    public String submitFeedback(@RequestParam("name") String name,
                                 @RequestParam("email") String email,
                                 @RequestParam("restaurantName") String restaurantName,
                                 @RequestParam("feedback") String feedback, Model model) {
        Feedback newFeedback = new Feedback(name, email, restaurantName, feedback);
        feedbackRepository.save(newFeedback);
        return "redirect:/feedback_success";
    }

    @GetMapping("/feedback_success")
    public String feedbackSuccess(){
        return "feedback_success";
    }
}
