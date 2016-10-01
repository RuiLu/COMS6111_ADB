import java.util.*;

public class Query {
	public Map<String, Integer> counts;
	public ArrayList<String> words;
	
	/*
	 * The initial function which gets an array of tokens and
	 * compute a map(from string to int) to present that Query.
	 * The map is used to store the counts of each token (which should be either 0 or 1).
	 */
	public Query(ArrayList<String> tokens) {
		words = new ArrayList<String>(tokens);
		counts = new HashMap<String, Integer>();
		for(int i=0;i<tokens.size();i++) {
			String token=tokens.get(i);
			if(counts.containsKey(token)) 
				counts.put(token, 1+counts.get(token));
			else
				counts.put(token, 1);
		}
		
	}
	
	/*
	 * Transform the Query into vector.
	 * Needs an index as input which tells the order of tokens
	 * in the vector, namely which is the i-th token.
	 * Here, for query, we use the tf-idf weights,
	 * so we also need a map which store the idf weights for all tokens.
	 * And the vector will be normalized too.
	 */
	public ArrayList<Double> toVector(ArrayList<String> index, Map<String, Integer> idf) {
		ArrayList<Double> res = new ArrayList<Double>();
		double sum = 0;
		Integer n = index.size();
		for(int i=0;i<index.size();i++) {
			double temp = 0;
			if(counts.containsKey(index.get(i)))
				temp = (double)counts.get(index.get(i)) * Math.log(n/idf.get(i));
			sum+=(temp*temp);
			res.add(temp);
		}
		sum = Math.sqrt(sum);
		for(int i=0;i<index.size();i++) {
			res.set(i, res.get(i)/sum);
		}
		
		return res;
	}
	
}
