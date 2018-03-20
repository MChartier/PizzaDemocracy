



public class PizzaScore {
	public int utility;
	public int vetoes;
	
	public PizzaScore(int utility, int vetoes) {
		this.utility = utility;
		this.vetoes = vetoes;
	}
	
	// Score exceeds another if it has fewer vetoes or 
	// same number of vetoes and a higher 'score' value
	public boolean higherThan(PizzaScore challenger) {
		return (this.vetoes < challenger.vetoes || 
			   (this.vetoes == challenger.vetoes && this.utility > challenger.utility));
	}
}
