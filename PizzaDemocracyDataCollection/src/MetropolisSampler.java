

import jsc.combinatorics.Permutations;
import jsc.combinatorics.Permutation;

public class MetropolisSampler {

	private Topping[] sigma;
	private double theta;
	
	private final int CONV_ITER = 500;
	private int[][] indexPairs;

	public MetropolisSampler(Topping[] sigma, double theta){
		this.sigma = sigma;
		this.theta = theta;	

		int indexPairID = 3;

		// iterate over all appropriate IDs
		int numPairs = binom(sigma.length, 2);
		indexPairs = new int[2][numPairs];
		for(int j = 0; j < numPairs; j++){
			// add pizza with current indexPairID to set of options
			int mask = 1;
			int k = 0;
			for(int i = 0; i < Topping.values().length; i++) {
				if(((mask & indexPairID) == mask)){
					indexPairs[k][j] = i;
					k++;
				}
				mask = (mask << 1);
			}

			// generate next indexPairID
			int tmp = (indexPairID | (indexPairID - 1)) + 1;  
			indexPairID = tmp | ((((tmp & -tmp) / (indexPairID & -indexPairID)) >> 1) - 1);
		}
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
    
	//calculates Kendall-tau distance
	private int d(Topping[] x, Topping[] y){
		int distance = 0;
		
		for(int i = 0; i < indexPairs[0].length; i++){
			if(!((trueRank(x[indexPairs[0][i]]) < trueRank(x[indexPairs[1][i]])) && (trueRank(y[indexPairs[0][i]]) < trueRank(y[indexPairs[1][i]])) ||
				 (trueRank(x[indexPairs[0][i]]) > trueRank(x[indexPairs[1][i]])) && (trueRank(y[indexPairs[0][i]]) > trueRank(y[indexPairs[1][i]])))){
				distance++;
			}			
		}		
			
		return distance;
	}

	private int trueRank(Topping topping){
		for(int i = 0; i < sigma.length; i++){
			if(sigma[i] == topping){
				return i;
			}
		}
		return 0;
	}

	public Topping[][] runAlgorithm(int numSamples){

		Topping[][] samples = new Topping[numSamples][sigma.length];
		Topping[] sigmaCur = new Topping[sigma.length];

		//generate random permutation of array sigma
		Permutations permutationGenerator = new Permutations(sigma.length);
		Permutation randomPermutation = permutationGenerator.randomPermutation();
		int[] randomPermutationInt = randomPermutation.toIntArray();
		for(int j = 0; j < sigma.length; j++){
			sigmaCur[j] = sigma[randomPermutationInt[j]-1];
		}

		Topping[] sigmaStar = sigmaCur.clone();

		//iterate until we have reached the convergence threshold and collected a sufficient number of items
		//TODO re-seed random
		for(int i = 0; i < CONV_ITER + numSamples; i++){			
			//swap two random elements
			int k = (int) (Math.random() * sigma.length);
			int l = (int) (Math.random() * sigma.length);

			sigmaStar[k] = sigmaCur[l];
			sigmaStar[l] = sigmaCur[k];

			int d1 = d(sigmaCur, sigma);
			int d2 = d(sigmaStar, sigma);

			if(d2 <= d1){
				sigmaCur = sigmaStar.clone();
			}
			else{
				double alpha = Math.pow(theta, d2-d1);
				double randomToss = Math.random();
				if(randomToss <= alpha){
					sigmaCur = sigmaStar.clone();
				}
				else{
					sigmaStar = sigmaCur.clone();
				}
			}

			//record sample if we're past the burnout threshold
			if(i >= CONV_ITER){
				samples[i - CONV_ITER] = sigmaCur.clone();
			}
		}
		
		return samples;

	}
}
