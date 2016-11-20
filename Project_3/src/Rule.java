import java.util.ArrayList;


public class Rule {
	private ArrayList<String> left;
	private String right;
	private double confidence;
	public Rule(ArrayList<String> left, String right, double confidence) {
		this.left=new ArrayList<String>(left);
		this.right=new String(right);
		this.confidence=confidence;
	}
	
	public ArrayList<String> get_left() {
		return left;
	}
	
	public String get_right() {
		return right;
	}
	
	public double get_confidence() {
		return confidence;
	}
	
}
