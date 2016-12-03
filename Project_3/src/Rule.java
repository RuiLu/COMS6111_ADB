import java.util.ArrayList;


public class Rule {
	private ArrayList<String> left;
	private String right;
	private double confidence;
	private double support;
	
	public Rule(ArrayList<String> left, String right, double confidence, double support) {
		this.left = new ArrayList<String>(left);
		this.right = "[" + new String(right) + "]";
		this.confidence = confidence;
		this.support = support;
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
	
	public double get_support() {
		return support;
	}
}
