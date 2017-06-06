package com.example.part2;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {

	// CLIENT VARIABLES
	private static Client client;
	private static ClientConfig config;
	private static WebTarget target;

	public static void main(String[] args) {
		// CONNECTION TO OUR WEB SERVICE
		connect();
		// GET ALL FOODS
		getFoods();
		// GET ALL FOODS ORDER BY QUANTITY
		getFoodsOrderByQuantity();
		// GET ALL FOODS BY NAME
		getFoodsByName("bio");
		// GET ALL FODS BY NAME & WHEN THE QUANTITY IS GREATHER THAN
		getFoodsByNameAndQuantity("bio", 1000);

		// CREATE FOOD
		Food food = new Food();
		food.set_id("300");
		food.setName("Tim Tam Original");
		food.setIngredients_translations(
				"Sucre, farine de blé, huile végétale (contient du soja), matière sèche de lait, beurre de cacao, pâte de cacao, mélasse claire, colorants alimentaires (caramel III (de blé), rouge de betterave, cochenille, rocou), cacao en poudre, émulsifiant (lécithine de soja, E476), sel, levure chimique, arôme. Contient 38% de chocolat au lait");
		food.setQuantity(200);
		food.setUnit("g");
		food.setPortion_quantity(20);
		food.setPortion_unit("g");

		NutrientsElements glucides = new NutrientsElements();
		glucides.setName_translations("Glucides");
		glucides.setUnit("g");
		glucides.setPer_hundred(16.0);
		glucides.setPer_portion(19.0);
		glucides.setPer_day(0.0);

		NutrientsElements sel = new NutrientsElements();
		sel.setName_translations("Sel");
		sel.setUnit("g");
		sel.setPer_hundred(0.0);
		sel.setPer_portion(0.0);
		sel.setPer_day(0.0);

		food.getNutrients().put("glucides", glucides);
		food.getNutrients().put("sel", sel);

		createFood(food);
		// GET THE NEW FOOD CREATED
		getFoodsByName("tim tam");

		// UPDATING FOOD
		food.setName("Tim Tam Original Coop");
		updateFood("300", food);

		// GET THE UPDATED FOOD
		getFoodsByName("coop");
		
		// DELETE A FOOD
		deleteFood("300");
		
		//GET THE DELETED FOOD
		getFoodsByName("coop");

	}

	// ALLOWS THE CONNECTION TO THE WEBSERVICE
	private static void connect() {
		System.out.println("----> INITIALISING CONNECTION...");
		config = new ClientConfig();
		client = ClientBuilder.newClient(config);
		target = client.target(getURI());
		System.out.println("----> CONNECTION SUCCESSFULL !");

	}

	private static URI getURI() {
		return UriBuilder.fromUri("http://localhost:8080/").build();
	}

	// METHOD TO GET ALL THE FOODS / PRODUCTS OF OUR WEBSERVICE
	private static void getFoods() {
		System.out.println("----> GETTING ALL FOODS...");
		String data = target.path("food").request().accept(MediaType.TEXT_PLAIN).header("Accept", "application/json")
				.get(String.class);
		prettyPrint(data);
	}

	private static void getFoodsOrderByQuantity() {
		System.out.println("----> GETTING ALL FOODS ORDER BY QUANTITY...");
		String data = target.path("food").path("quantity").request().accept(MediaType.TEXT_PLAIN)
				.header("Accept", "application/json").get(String.class);
		prettyPrint(data);

	}

	private static void getFoodsByName(String name) {
		System.out.println("----> GETTING ALL FOODS BY NAME : " + name);
		String data = target.path("food").queryParam("name", name).request().accept(MediaType.TEXT_PLAIN)
				.header("Accept", "application/json").get(String.class);
		prettyPrint(data);
	}

	private static void getFoodsByNameAndQuantity(String name, int quantity) {
		System.out.println("----> GETTING ALL FOODS BY NAME : " + name + " AND QUANTITY : " + quantity);
		String data = target.path("food").queryParam("name", name).queryParam("quantity", quantity).request()
				.accept(MediaType.TEXT_PLAIN).header("Accept", "application/json").get(String.class);
		prettyPrint(data);
	}

	private static void createFood(Food food) {
		System.out.println("----> CREATING A FOOD : " + food.getName());
		ObjectMapper mapper = new ObjectMapper();
		String foodJSONString;
		try {
			foodJSONString = mapper.writeValueAsString(food);
			Response response = target.path("food").request().accept(MediaType.TEXT_PLAIN)
					.header("Accept", "application/json").post(Entity.json(foodJSONString));
			System.out.println("----> CREATION OF FOOD SUCCESSFULL");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateFood(String id, Food food) {
		System.out.println("----> UPDATING A FOOD : " + food.getName() + " WITH ID : " + id);
		ObjectMapper mapper = new ObjectMapper();
		String foodJSONString;
		try {
			foodJSONString = mapper.writeValueAsString(food);
			Response response = target.path("food").path(id).request().accept(MediaType.TEXT_PLAIN)
					.header("Accept", "application/json").put(Entity.json(foodJSONString));
			System.out.println("----> UPDATE OF FOOD SUCCESSFULL");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void deleteFood(String id) {
		System.out.println("----> DELETING A FOOD WITH ID : " + id);

		Response response = target.path("food").path(id).request().accept(MediaType.TEXT_PLAIN).header("Accept", "application/json").delete();
		System.out.println("----> DELETE OF FOOD SUCCESSFULL");
	}

	private static void prettyPrint(String jsonData) {
		JSONArray json;
		try {
			json = new JSONArray(jsonData);
			System.out.println(json.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Convert text to object

	}

}
