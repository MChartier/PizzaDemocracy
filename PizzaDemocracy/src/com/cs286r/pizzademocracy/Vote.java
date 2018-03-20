package com.cs286r.pizzademocracy;

import java.util.ArrayList;
import jsc.combinatorics.Permutation;
import jsc.combinatorics.Permutations;

/*
 *  Vote.java
 *  Stores an agent's preferences and vetoes.
 */

public class Vote {
	private Topping[] prefs;
	private Topping[] vetoes;
	
	public Vote(Topping[] prefs, Topping[] vetoes) {
		this.prefs = prefs;
		this.vetoes = vetoes;
	}
	
	public int numPrefs() {
		return prefs.length;
	}
	
	public int numVetoes() {
		return vetoes.length;
	}
	
	// was this topping ranked among preferences?
	public boolean rankedTopping(Topping topping) {
		for(int i = 0; i < this.prefs.length; i++) {
			if(prefs[i] == topping)
				return true;
		}
		return false;
	}
	
	// what is the rank of this topping?
	// (assumes topping was in fact ranked in prefs!)
	public int rankTopping(Topping topping) {
		for(int i = 0; i < this.prefs.length; i++) {
			if(prefs[i] == topping) {
				return i;
			}
		}
		// TODO: throw error
		return -1;
	}
	
	// was this topping vetoed by this agent?
	public boolean vetoedTopping(Topping topping) {
		for(int i = 0; i < this.vetoes.length; i++) {
			if(vetoes[i] == topping) {
				return true;
			}
		}
		return false;
	}
	
	// does this pizza contain a topping vetoed by this agent?
	public boolean vetoedPizza(Pizza pizza) {
		for(int i = 0; i < vetoes.length; i++) {
			if(pizza.hasTopping(vetoes[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static Vote random(int numPrefs, int numVetoes) {
		
		Topping[] toppings = Topping.values();
		int numAlts = toppings.length;
		
		if(numPrefs + numVetoes > numAlts)
			return null;
		
		// generate random permutations which determine our randoms preferences and 
		Permutations permutationGenerator = new Permutations(numAlts);
		Permutation permutation = permutationGenerator.randomPermutation();
		int[] permutationArray = permutation.toIntArray();
		
		Topping[] prefs = new Topping[numPrefs];
		Topping[] vetoes = new Topping[numVetoes];
		
		int index = 0;
		for(int i = 0; i < numPrefs; i++, index++) {
			prefs[i] = toppings[permutationArray[index]];
		}
		
		for(int i = 0; i < numVetoes; i++, index++) {
			vetoes[i] = toppings[permutationArray[index]];
		}
		
		return new Vote(prefs, vetoes);
	}
	
	
	/*
	 * public Vote restrictVote(int numPrefs, int numVetoes)
	 * 
	 * Restrict a vote to some maximum number of 
	 */
	public Vote restrictVote(int numPrefs, int numVetoes) {
		if (numPrefs > this.prefs.length)
			numPrefs = this.prefs.length;
		if (numVetoes > this.vetoes.length)
			numVetoes = this.vetoes.length;
		
		Topping[] newPrefs = new Topping[numPrefs];
		Topping[] newVetoes = new Topping[numVetoes];
		
		for(int i = 0; i < numPrefs; i++) {
			newPrefs[i] = this.prefs[i];
		}
		for(int i = 0; i < numVetoes; i++) {
			newVetoes[i] = this.vetoes[i];
		}
		
		return new Vote(newPrefs, newVetoes);
	}
}
