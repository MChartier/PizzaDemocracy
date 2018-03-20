package com.cs286r.pizzademocracy;

/*
 *  Borda.java
 *  
 *  Implements the Elector class using the Borda scoring rule.
 *  Scores partial profiles using 'worst-case' completions and selects
 *  a maximin winner.
 */

public class Borda implements Elector {
	// return a score given a rank and number of alternatives
	private int scoreRank(int rank, int numAlts) {
		return numAlts - rank - 1;
	}
	
	/*
	 * public Score scorePizza(Profile profile, Pizza pizza)
	 *  
	 * Given a partial preference profile and a pizza, calculate the
	 * worst-case score assigned by each voter the given pizza, then 
	 * returns the minimal score assigned by the voters.
	 */
	public PizzaScore scorePizza(Profile profile, Pizza pizza) {
		int totalVetoes = 0;
		int voteRank, worstOpenRanking;
		int numAlts = Topping.values().length; // hard-coded assumption that we are ranking over all possible toppings
		int numVotes = profile.numVotes();

		// record how each voter scores the pizza
		int[] scores = new int[numVotes];
		
		// calculate scores and vetoes across agents
		for(int i = 0; i < numVotes; i++) {
			Vote vote = profile.getVote(i);
			
			// record veto if there is one
			if(vote.vetoedPizza(pizza))
				totalVetoes++;
			
			// adversarially calculate local score in worst case
			worstOpenRanking = numAlts - vote.numVetoes() - 1;
			scores[i] = 0;
		
			// calculate voter's score for each topping
			Topping[] pizzaToppings = pizza.getToppings();
			for(int j = 0; j < pizzaToppings.length; j++) {
				Topping topping = pizzaToppings[j];
				
				// assign score as ranked or adversarially
				if(vote.rankedTopping(topping)) {
					voteRank = vote.rankTopping(topping);
				}
				else if(vote.vetoedTopping(topping)) {
					voteRank = numAlts - 1;
				}
				else {
					voteRank = worstOpenRanking;
					worstOpenRanking--;
				}
				
				scores[i] += scoreRank(voteRank, numAlts);
			}
		} 
		
		// find minimum score for pizza across all agents
		int minScore = Integer.MAX_VALUE; // suitably loose upper bound
		for(int i = 0; i < numVotes; i++) {
			if(scores[i] < minScore)
				minScore = scores[i];
		}
		
		return new PizzaScore(minScore, totalVetoes);
	}
	
	
	public PizzaScore scorePizza(Vote vote, Pizza pizza) {
		int vetoes = 0;
		int numAlts = Topping.values().length;
		
		Topping[] toppings = pizza.getToppings();
		
		for(int i = 0; i < toppings.length; i++) {
			if(vote.vetoedTopping(toppings[i])) {
				vetoes++;
			}
		}
		
	    // adversarially calculate local score in worst case
		int worstOpenRanking = numAlts - vote.numVetoes() - 1;
		int utility = 0;
		int voteRank;
		
		for(int j = 0; j < toppings.length; j++) {
			Topping topping = toppings[j];
						
			// assign score as ranked or adversarially
			if(vote.rankedTopping(topping)) {
				voteRank = vote.rankTopping(topping);
			}
			else if(vote.vetoedTopping(topping)) {
				voteRank = numAlts - 1;
			}
			else {
				voteRank = worstOpenRanking;
				worstOpenRanking--;
			}
				
			utility += scoreRank(voteRank, numAlts);
		}
		
		return new PizzaScore(utility, vetoes);
	}
	
	/*
	 * public Pizza choosePizza(Profile profile, Topping[][] pizzaOptions)
	 * 
	 * Given a partial preference profile and a set of pizzas, returns a pizza
	 * which best satisfies the least-satisfied agent.
	 */
    public Pizza choosePizza(Profile profile, Pizza[] pizzaOptions) {
		Pizza currPizza, topPizza = null;
		PizzaScore currScore, topScore = null;
		
		// calculate score for each pizza and remember the highest-scoring option
		for(int i = 0; i < pizzaOptions.length; i++) {
			currPizza = pizzaOptions[i];
			currScore = scorePizza(profile, pizzaOptions[i]);
			if(topPizza == null || currScore.higherThan(topScore)) {
				topPizza = currPizza;
				topScore = currScore;
			}
		} 
		
		return topPizza;
	}
    
    public Pizza choosePizza(Profile profile) {   	
    	return choosePizza(profile, genPizzaOptions(Topping.values().length));
    }
    
    public Pizza choosePizza(Profile profile, int maxNumToppings) {
    	return choosePizza(profile, genPizzaOptions(maxNumToppings));
    }
    
    public Pizza[] genPizzaOptions(int numToppings) {
    	int pizzaID; // current permutation of bits

    	int tmp;
    	int numAlts = Topping.values().length;
    	int index = 0;
    	int numPizzas = 0;
    	
    	for(int i = 0; i <= numToppings; i++) {
    		numPizzas += binom(numAlts, i);
    	}
    	Pizza[] pizzaOptions = new Pizza[numPizzas];
    	
    	pizzaOptions[0] = new Pizza(0);
    	index++;
    	
    	// for each number of toppings, iterate over bitstrings with that many 1s
    	// these are the 'pizza IDs' of pizzas with that many toppings
    	for(int i = 1; i <= numToppings; i++) {
    		pizzaID = 0;
    		
    		// prepare first appropriate pizza ID
    		for(int j = 0; j < i; j++) {
    			pizzaID = (pizzaID << 1);
    			pizzaID += 1;
    		}
    		
    		// iterate over all appropriate IDs
    		int subNumPizzas = binom(numAlts, i);
    		for(int j = 0; j < subNumPizzas; j++) {
    			// add pizza with current pizzaID to set of options
    			pizzaOptions[index] = new Pizza(pizzaID);
    			index++;
    			
    			// generate next pizzaID
    			tmp = (pizzaID | (pizzaID - 1)) + 1;  
    			pizzaID = tmp | ((((tmp & -tmp) / (pizzaID & -pizzaID)) >> 1) - 1);
    		}
    	}
    	
    	// ensure we generated exactly enough pizzas
    	assert(index == numPizzas);
    	
    	return pizzaOptions;
    }
    
    private int binom(int n, int k) {
    	if(k > n)
    		return -1;
    	
    	return (int) (fact(n) / (fact(n-k) * fact(k)));
    }
    
    private long fact(int n) {
    	long product = 1;
    	for (long i = 1; i <= n; i++) {
    		product *= i;
    	}
    	return product;
    }
   
    public Order chooseOrder(Profile profile, int numPizzas, int numToppings) {
    	// all possible pizzas with <= numToppings
    	Pizza[] pizzaOptions = genPizzaOptions(numToppings);
    	
    	int numPizzaOptions = pizzaOptions.length;
    	
    	int numVotes = profile.numVotes();
    	PizzaScore[][] pizzaScores = new PizzaScore[numVotes][numPizzaOptions];
    	    	
    	// pre-score each possible pizza for each voter
    	for(int i = 0; i < numVotes; i++) {
    		for(int j = 0; j < pizzaOptions.length; j++) {
    			pizzaScores[i][j] = scorePizza(profile.getVote(i), pizzaOptions[j]);
    		}
    	}
    	
    	int numPossibleOrders = (int) Math.pow(pizzaOptions.length, numPizzas);
    	
    	// build up pizza arrays to store in our orders are we generate them
    	Pizza currPizzas[] = null;
    	Order currOrder = null;
    	Order topOrder = null;
    	OrderScore currScore = null;
    	OrderScore topScore = null;
    	
    	// we store indices to simulate 'numPizzas' nested for loops
    	int[] indices = new int[numPizzas];
    	for(int i = 0; i < numPizzas; i++) {
    		indices[i] = 0;
    	}
    	
    	// to generate each of our orders...
    	int orderIndex;
    	for(orderIndex = 0; orderIndex < numPossibleOrders; orderIndex++) {
    		
    		// we increment the first (most deeply nested) for loop counter
    		for(int x = 0; x < numPizzas; x++) {
    			indices[x] = (indices[x] + 1) % numPizzaOptions;
    			
    			// if we roll over to 0, we'll have to hop up to the next index and also increment that one
    			if(indices[x] != 0) {

    				// indices define the pizzas included and their order
    				currPizzas = new Pizza[numPizzas];
    				for(int j = 0; j < numPizzas; j++) {
    					currPizzas[j] = pizzaOptions[indices[j]];
    				}
    				
    				currOrder = new Order(currPizzas);
    				
    				currScore = scoreOrder(profile, currOrder);
    				
    				if(topScore == null || currScore.higherThan(topScore)) {
    					topScore = currScore;
    					topOrder = currOrder;
    				}
    				
    				break;
    			}
    		}
    	}
    	
    	return topOrder;
    }
    
    // maximize the minimum amount an agent likes any pizza
 	public OrderScore scoreOrder(Profile profile, Order order) {
 		int numVotes = profile.numVotes();
 		int numPizzas = order.numPizzas();
 		 		
 		PizzaScore pizzaScore, voterScore, orderScore = null;
 		int orderUtility = 0;
 		
 		Vote currVote;
 		
 		// each voter scores all pizzas in the order
 		for(int i = 0; i < numVotes; i++) {
 			currVote = profile.getVote(i);
 			voterScore = null;
 			
 			// determine which pizza the voter likes the most
 			for(int j = 0; j < numPizzas; j++) {
 				pizzaScore = scorePizza(currVote, order.getPizza(j));
 				
 				// track how much utility voters see in this order in the aggregate
 				if(pizzaScore.vetoes == 0)
 					orderUtility += pizzaScore.utility;
 				
 				if(voterScore == null || pizzaScore.higherThan(voterScore)) {
 					voterScore = pizzaScore;
 				}
 			}
 	
 			// update minimum score for the order in the profile
 			if(orderScore == null || orderScore.higherThan(voterScore)) {
 				orderScore = voterScore;
 			}
 		}
 		
 		// return lowest score any voter assigned to the order
 		return new OrderScore(orderScore, orderUtility);
 	}   
}