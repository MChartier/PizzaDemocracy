package com.cs286r.pizzademocracy;

import java.util.ArrayList;

import jsc.combinatorics.Permutation;
import jsc.combinatorics.Permutations;

/* 
 * Profile.java
 * Profile of partial votes for each agent.
 */

public class Profile {
	private Vote[] votes;
	
	public Profile(ArrayList<Vote> votes) {
	    Object[] voteObjectArray = votes.toArray();
		
		this.votes = new Vote[voteObjectArray.length];
		for(int i = 0; i < this.votes.length; i++) {
			this.votes[i] = (Vote) voteObjectArray[i];
		}
	}
	
	public Profile(Topping[][] fullRanking, int numPrefs, int numVetoes) {
		int numAlts = Topping.values().length;
		int numVotes = fullRanking.length;
		Vote[] votes = new Vote[numVotes];
		
		Topping[] currVoterPrefs = new Topping[numPrefs];
		Topping[] currVoterVetoes = new Topping[numVetoes];
		for(int i = 0; i < numVotes; i++) {
			for(int j = 0; j < numPrefs; j++) {
				currVoterPrefs[j] = fullRanking[i][j];
			}
			for(int j = 0; j < numVetoes; j++) {
				currVoterVetoes[j] = fullRanking[i][numAlts - numVetoes + j];
			}
			votes[i] = new Vote(currVoterPrefs, currVoterVetoes);
		}
		
		this.votes = votes;
	}
	
	public Profile(Vote[] votes) {
		this.votes = votes;
	}
	
	public Vote getVote(int index) {
		
		return votes[index];
	}
	
	public int numVotes() {
		return votes.length;
	}
	
	public static Profile random(int numVotes, int numPrefs, int numVetoes) {
		Vote[] votes = new Vote[numVotes];
		Topping[] toppings = Topping.values();
		int numAlts = toppings.length;

		if(numPrefs + numVetoes > numAlts)
			return null;
		
		Permutations permutationGenerator = new Permutations(numAlts);
		Permutation permutation;
		int[] permutationArray;
		Topping[] prefs, vetoes = null;
		
		// generate a set of random votes
		for(int i = 0; i < numVotes; i++) {
			
			// generate random permutations which determine our randoms preferences and 
			permutation = permutationGenerator.randomPermutation();	
			permutationArray = permutation.toIntArray();
			
			prefs = new Topping[numPrefs];
			vetoes = new Topping[numVetoes];
			
			int index = 0;
			for(int j = 0; j < numPrefs; j++, index++) {
				prefs[j] = toppings[permutationArray[index] - 1];
			}
			
			for(int j = 0; j < numVetoes; j++, index++) {
				vetoes[j] = toppings[permutationArray[index] - 1];
			}
			
			votes[i] = new Vote(prefs, vetoes);
		}
		
		return new Profile(votes);
	}
	
	public Profile restrict(int numPrefs, int numVetoes) {
		Vote[] restrictedVotes = new Vote[this.numVotes()];
		
		for(int i = 0; i < this.votes.length; i++) {
			restrictedVotes[i] = votes[i].restrictVote(numPrefs, numVetoes);
		}
		
		return new Profile(restrictedVotes);
	}
}
