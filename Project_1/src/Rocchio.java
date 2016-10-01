import java.util.*;

public class Rocchio {
	Map<String, Integer> dictionary ;
	ArrayList<String> index;
	Map<String, Integer> idf;
	ArrayList<Double> qt;
	
	private void update_index(String token) {
		if(!dictionary.containsKey(token)) {
			dictionary.put(token, index.size());
			index.add(token);
			idf.put(token, 1);
		} else
			idf.put(token, 1+idf.get(token));
	}
	
	/*
	 * Using tokens of docs to update index and idf. 
	 */
	private void add_docs(ArrayList<Document> docs) {
		for(int i=0;i<docs.size();i++) {
			for(Map.Entry<String, Integer> pair : docs.get(i).counts.entrySet()) {
				String token = pair.getKey();
				update_index(token);
			}
		}
	}
	
	/*
	 * Transform docs into vectors and update the vector qt.
	 */
	private void update_vector(ArrayList<Document> docs, double param) {
		ArrayList<Double> temp = new ArrayList<Double>(qt.size());
		ArrayList<Double> doc;
		for(int i=0;i<docs.size();i++) {
			doc = docs.get(i).toVector(index);
			for(int j=0;j<qt.size();j++)
				temp.set(j, temp.get(j)+doc.get(j));
		}
		for(int i=0;i<qt.size();i++)
			qt.set(i, qt.get(i)+param*temp.get(i)/docs.size());
	}
	
	public ArrayList<String> Rocchio_Algorithm(
			Query q, 
			ArrayList<Document> relevant, 
			ArrayList<Document> nonrelevant
			) {
		double alpha = 1;
		double beta = 0.75;
		double gamma = -0.25;

		
		dictionary = new HashMap<String, Integer>();
		index = new ArrayList<String>();
		idf = new HashMap<String, Integer>();
		
		add_docs(relevant); // Add tokens in relevant docs into index.
		add_docs(nonrelevant); // Add tokens in non-relevant docs into index.

		
		qt = q.toVector(index, idf); 
		for(int i=0;i<qt.size();i++) qt.set(i,alpha*qt.get(i));
		update_vector(relevant, beta);
		update_vector(nonrelevant, gamma);
		
		ArrayList<String> res = new ArrayList<String>();
		
		return res;
	}
}
