import java.util.*;

public class Document {
	public Map<String, Integer> counts;
	
	/*
	 * The initial function which gets an array of tokens and
	 * compute a map(from string to int) to present that document.
	 * The map is used to store the counts of each token.
	 */
	public Document(ArrayList<String> tokens) {
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
	 * Transform the Document into vector.
	 * Needs an index as input which tells the order of tokens
	 * in the vector, namely which is the i-th token.
	 * Here we only uses the tf weight and normalize each vector.
	 */
	public ArrayList<Double> toVector(ArrayList<String> index) {
		ArrayList<Double> res = new ArrayList<Double>();
		double sum = 0;
		for(int i=0;i<index.size();i++) {
			double temp = 0;
			if(counts.containsKey(index.get(i)))
				temp = (double)counts.get(index.get(i));
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
