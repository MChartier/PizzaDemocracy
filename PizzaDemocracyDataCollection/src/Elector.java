

/*
 *  Elector.java
 *  
 *  Scores a set of options given a profile and
 *  returns a winning option.
 */

public interface Elector {
	PizzaScore scorePizza(Profile profile, Pizza pizza);
	Pizza choosePizza(Profile profile, Pizza[] pizzaOptions);
	OrderScore scoreOrder(Profile profile, Order order);
	Order chooseOrder(Profile profile, int numPizzas, int numToppings);
}