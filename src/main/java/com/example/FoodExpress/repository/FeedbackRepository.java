package com.example.FoodExpress.repository;


import com.example.FoodExpress.entity.Feedback;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
