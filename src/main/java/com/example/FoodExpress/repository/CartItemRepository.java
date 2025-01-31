package com.example.FoodExpress.repository;

import com.example.FoodExpress.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByItemId(String itemId);
}
