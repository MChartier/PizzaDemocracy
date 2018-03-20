public class PizzaDemocracyDataCollection {

	public static void main(String[] args) {
		Borda bordaElector = new Borda();
		Order order = null;
		int numAlts = Topping.values().length;

		// experimental variables
		//float theta = (float) 0.5;
		int sampleSize = 100;
		int numPrefs = 3;
		int numVetoes = 1;
		//int numPizzas = 2;
		//int numToppings = 3;
		int trials = 100;
		double[] theta = {0.1,0.25,0.5,0.75,1};
		MetropolisSampler sampler;
		Topping[][] fullRanking;
		int[][] values;
		
		for(int thetaIndex = 0; thetaIndex < theta.length; thetaIndex++) {
			sampler = new MetropolisSampler(Topping.values(), theta[thetaIndex]);
			for(int numPizzas = 3; numPizzas <= 3; numPizzas++) {
				for(int numToppings = 2; numToppings <= 2; numToppings++) {

					// partialVeto, fullVeto, partialBorda, fullBorda
					Profile[] testProfiles = new Profile[4];
					OrderScore[] testScores = new OrderScore[4];

					values = new int[trials][12];

					// for each trial...
					for(int i = 0; i < trials; i++) {

						// sample a full ranking
						fullRanking = sampler.runAlgorithm(sampleSize);

						// generate types of profiles
						testProfiles[0] = new Profile(fullRanking, numPrefs, numVetoes); // partial with veto
						testProfiles[1] = new Profile(fullRanking, numAlts - numVetoes, numVetoes); // full with veto
						testProfiles[2] = new Profile(fullRanking, numPrefs + numVetoes, 0); // partial Borda
						testProfiles[3] = new Profile(fullRanking, numAlts, 0); // full Borda

						// get outcomes for four types of profiles
						for(int j = 0; j < 4; j++) {
							order = bordaElector.chooseOrder(testProfiles[j], numPizzas, numToppings);
							testScores[j] = bordaElector.scoreOrder(testProfiles[1], order);

							values[i][(3*j)] = testScores[j].score;
							values[i][(3*j) + 1] = testScores[j].vetoes;
							values[i][(3*j) + 2] = testScores[j].utility;
						}			

						System.out.println("Trial " + (i + 1) + " complete.\n");
					}

					CSVGenerator.writeCSV(values, theta[thetaIndex] + "-" + numPizzas + "-" + numToppings + ".txt");
				}
			}
		}
	}
}
