package com.example.FoodExpress.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CartItem {
    @Id
    private String id;
    private String itemId;
    private int quantity;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

