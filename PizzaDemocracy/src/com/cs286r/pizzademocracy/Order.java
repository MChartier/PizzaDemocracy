package com.cs286r.pizzademocracy;

/*
 *  Order.java
 * 
 *  An order is a set of pizzas.
 *  NOT AN ORDERING OF PIZZAS. An order, like, one you would place
 */

public class Order {
	private Pizza[] pizzas;
	
	public Order(Pizza[] pizzas) {
		this.pizzas = pizzas;
	}
	
	public Pizza getPizza(int index) {
		return pizzas[index];
	}
	
	public int numPizzas() {
		return pizzas.length;
	}
}
