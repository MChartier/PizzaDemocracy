package com.cs286r.pizzademocracy;

/*
 *  Pizza.java
 * 
 *  A pizza is a set of toppings.
 */

public class Pizza {
	private boolean[] toppings; 
	private int numToppings;
	
	public Pizza(boolean[] toppings) {
		this.toppings = toppings;
		
		numToppings = 0;
		for(int i = 0; i < toppings.length; i++){
			if(toppings[i]){
				numToppings++;
			}
		}
	}
	
	public Pizza(int pizzaId) {
		int numAllToppings = Topping.values().length;
		boolean[] toppings = new boolean[numAllToppings];
		int mask = 1;
		
		int toppingCount = 0;
		
		// set toppings based on number
		for(int j = 0; j < numAllToppings; j++) {
			toppings[j] = ((mask & pizzaId) == mask);
			if(toppings[j])
				toppingCount++;
			mask = (mask << 1);
		}
		
		this.toppings = toppings;
		numToppings = toppingCount;
	}
	
	public Topping[] getToppings() {
		Topping[] presentToppings = new Topping[numToppings];
		int index = 0;
		for(int i = 0; i < toppings.length && index < numToppings; i++) {
			if(toppings[i]) {
				presentToppings[index] = Topping.values()[i];
				index++;
			}
		}
		return presentToppings;
	}
	
	public boolean hasTopping(Topping topping) {
		Topping[] toppingValues = Topping.values();
		for(int i = 0; i < toppings.length; i++) {
			if(toppingValues[i] == topping) {
				return toppings[i];
			}
		}
		
		// but Java, this never happens!!1one!
		return false;
	}
	
	public int numToppings() {	
		return numToppings;
	}
}
