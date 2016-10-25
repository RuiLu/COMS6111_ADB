/**
 * Query - a specific class for query
 */
import java.util.HashMap;
import java.util.ArrayList;

public class Query {
	private HashMap<String, Integer> tfMap;
	private ArrayList<String> words;
	
	/**
	 * The initial function which gets an array of tokens and
	 * compute a map(from string to int) to present that Query.
	 * The map is used to store the counts of each token (which should be either 0 or 1).
	 * @param query -> input query in String, each key word is separated by a whitespace 
	 */
	public Query(String query) {
		String[] tokens = query.split(" ");
		words = new ArrayList<String>();
		
		for (String token : tokens) {
			words.add(token);
		}
		
		tfMap = new HashMap<String, Integer>();
		for(int i = 0; i < words.size(); i++) {
			String token = words.get(i);
			if (tfMap.containsKey(token)) 
				tfMap.put(token, tfMap.get(token) + 1);
			else {
				tfMap.put(token, 1);
			}
		}
		
	}

	/**
	 * Used to get terms' weights from query
	 * The method here to compute weight is using tf-idf values.
	 * Formula: tf-idf = tf * log(n/df)
	 * @param termsWeights, passed from Rocchio, used for storing terms and its weight
	 */
	public void getTermsWeight(HashMap<String, Integer> dfMap,
							   HashMap<String, Double> termsWeights) {
		
		for (String key : words) {
			double df = dfMap.containsKey(key) ? dfMap.get(key) : 0.0;
			double idf = df != 0.0 ? Math.log10(10.0 / df) : 0.0;
			
			if (!termsWeights.containsKey(key)) {
				termsWeights.put(key, 0.0);
			}
			termsWeights.put(key, termsWeights.get(key) + 1.0 * idf);
		}
	}
	
	/**
	 * Return query in String format, each key is separated by a whitespace
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (; i < words.size() - 1; i++) {
			sb.append(words.get(i) + " ");
		}
		sb.append(words.get(i));
		return sb.toString();
	}
	
	/**
	 * Return the term frequency map of query 
	 * @return 
	 */
	public HashMap<String, Integer> getTFMap() {
		return tfMap;
	}
}
