package com.example.FoodExpress.repository;

import com.example.FoodExpress.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {

}
