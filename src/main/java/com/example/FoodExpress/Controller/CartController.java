package com.example.FoodExpress.Controller;

import com.example.FoodExpress.entity.CartItem;
import com.example.FoodExpress.entity.Item;
import com.example.FoodExpress.repository.ItemRepository;
import com.example.FoodExpress.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/add/{itemId}")
    public ResponseEntity<String> addToCart(@PathVariable String itemId) {
        System.out.println("Adding item to cart: " + itemId);
        cartService.addItemToCart(itemId);
        return ResponseEntity.ok("Item added to cart");
    }
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Integer>> getCartSummary()
    {
        Map<String, Integer> cartSummary = cartService.getCartSummary();
        System.out.println("Cart Summary: " + cartSummary);
        return ResponseEntity.ok(cartSummary);
    }


    @GetMapping
    public String viewCart(Model model) {
        return "cart";
    }


    }


