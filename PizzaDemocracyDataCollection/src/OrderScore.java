

public class OrderScore {
	public int score;
	public int vetoes;
	public int utility;
	
	public OrderScore(int score, int vetoes, int utility) {
		this.score = score;
		this.vetoes = vetoes;
		this.utility = utility;
	}
	
	public OrderScore(PizzaScore orderScore, int orderUtility) {
		this.score = orderScore.utility;
		this.vetoes = orderScore.vetoes;
		this.utility = orderUtility;
	}

	// Score exceeds another if it has fewer vetoes or 
	// same number of vetoes and a higher 'score' value
	public boolean higherThan(OrderScore challenger) {
		return (this.vetoes < challenger.vetoes || 
			   (this.vetoes == challenger.vetoes && this.score > challenger.score) ||
			   (this.vetoes == challenger.vetoes && this.score == challenger.score && this.utility > challenger.utility));
	}
}
