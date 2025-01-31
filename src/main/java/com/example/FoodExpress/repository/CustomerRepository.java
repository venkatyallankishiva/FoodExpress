package com.example.FoodExpress.repository;



import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<com.example.foodexpress.model.Customer, Long> {
}
