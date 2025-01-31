package com.example.FoodExpress.service;

import com.example.FoodExpress.entity.CartItem;
import com.example.FoodExpress.repository.CartItemRepository;
import com.example.FoodExpress.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

private Map<String, Integer> cart = new HashMap<>();
public void addItemToCart(String itemId) {
    cart.put(itemId, cart.getOrDefault(itemId, 0) + 1);
}
public Map<String, Integer> getCartSummary() {
    return cart;
}


    public List<CartItem> getCartItems() {
        return cartItemRepository.findAll();
    }
}

