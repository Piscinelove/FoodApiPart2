package com.example.part2;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface FoodRepository extends MongoRepository<Food, String> {
	
	//FIND ALL FOOD
	//ORDER BY QUANTITY
	List<Food> findAllByOrderByQuantity();
	//FIND ALL FOOD CONTAINING THE NAME
	//IGNORE CASES
	//ORDER BY NAME 
	List<Food> findByNameContainsIgnoreCaseOrderByNameAsc(String name);
	
	//FIND ALL FOOD CONTAINING THE NAME
	//AND WHEN THEIR QUANTITY IS GREATHER THAN
	//IGNORE CASES
	//ORDER BY NAME 
	//ORDER BY QUANTITY
	List<Food> findByNameContainsIgnoreCaseAndQuantityGreaterThanEqualOrderByNameAscQuantityAsc(String name, int quantity);

}
