package com.example.part2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/food")
public class FoodRestController {
	
	//FOOD REPOSITORY
	@Autowired
	private FoodRepository repo;
	
	//GET ALL FOODS
	@RequestMapping(method = RequestMethod.GET)
	public List<Food> getFoods() {
		return repo.findAll();
	}
	
	//GET ALL FOODS ORDER BY QUANTITY
	@RequestMapping(method = RequestMethod.GET, path = "/quantity")
	public List<Food> getFoodsOrderByQuantity() {
		return repo.findAllByOrderByQuantity();		
	}
	
	//GET ALL FOODS BY NAME
	@RequestMapping(method = RequestMethod.GET, params = { "name" })
	public @ResponseBody List<Food> getFoodsByName_Translations(@RequestParam(value = "name") String name) {
		return repo.findByNameContainsIgnoreCaseOrderByNameAsc(name);
	}
	
	//GET ALL FOODS BY NAME AND QUANTITY
	@RequestMapping(method = RequestMethod.GET, params = { "name", "quantity" })
	public @ResponseBody List<Food> getFoodsByName_TranslationsAndQuantity(@RequestParam(value = "name") String name, @RequestParam(value = "quantity") int quantity) {
		return repo.findByNameContainsIgnoreCaseAndQuantityGreaterThanEqualOrderByNameAscQuantityAsc(name, quantity);
	}
	
	//CREATE A NEW FOOD
	@RequestMapping(method=RequestMethod.POST)
	public Food createFood(@RequestBody Food food)
	{
		return repo.save(food);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public @ResponseBody void delete(@PathVariable String id) {
		repo.delete(id);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Food update(@PathVariable String id, @RequestBody Food food) {
		
		Food foodToUpdate = repo.findOne(id);
		foodToUpdate.setName(food.getName());
		foodToUpdate.setIngredients_translations(food.getIngredients_translations());
		foodToUpdate.setQuantity(food.getQuantity());
		foodToUpdate.setUnit(food.getUnit());
		foodToUpdate.setPortion_quantity(food.getPortion_quantity());
		foodToUpdate.setPortion_unit(food.getPortion_unit());
		foodToUpdate.setNutrients(food.getNutrients());

		return repo.save(foodToUpdate);
	}

	
	

}
