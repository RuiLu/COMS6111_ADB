/**
 * Document - a specific class for document
 */
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Document {
	private HashMap<String, Integer> tfMap;
	
	/**
	 * The initial function which gets a list of tokens and
	 * compute a map(from string to int) to present that document.
	 * The map is used to store the counts of each token.
	 * @param tokens -> an ArrayList containing all terms from a document
	 */
	public Document(ArrayList<String> tokens) {
		tfMap = new HashMap<String, Integer>();
		
		for(String token : tokens) {
			if(tfMap.containsKey(token)) 
				tfMap.put(token, tfMap.get(token) + 1);
			else
				tfMap.put(token, 1);
		}
	}
	

	/**
	 * Used to get terms' weights from each document
	 * The method here to compute weight is using Euclidean normalized tf values.
	 * Formula: according to Introduction to Information Retrieval, Section 6.3
	 * @param termsWeights, passed from Rocchio, used for storing terms and its weight
	 */
	public void getTermsWeights(HashMap<String, Double> termsWeights) {
		double quadraticSum = 0;
		double rootQuadraticSum = 0;
		
		for (Map.Entry<String, Integer> me : tfMap.entrySet()) {
			int tf = me.getValue();
			quadraticSum += tf * tf;
		}
		rootQuadraticSum = Math.sqrt(quadraticSum);
		
		for (Map.Entry<String, Integer> me : tfMap.entrySet()) {
			String term = me.getKey();
			int tf = me.getValue();
			double weight = tf / rootQuadraticSum;
			
			if (!termsWeights.containsKey(term)) {
				termsWeights.put(term, 0.0);
			}
			termsWeights.put(term, termsWeights.get(term) + weight);
		}
		
	}
	
	/**
	 * Return term frequency map of a document
	 * @return
	 */
	public HashMap<String, Integer> getTFMap() {
		return tfMap;
	}
}
