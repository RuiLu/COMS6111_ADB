/**
 * A class for implementing Rocchio's Algorithm.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

public class Rocchio {
	private final double alpha = 1.0;
	private final double beta = 0.75;
	private final double gamma = -0.15;
	
	private TreeMap<String, Double> termsWeights = null;
	private HashMap<String, Double> queryWeightMap = null;
	private HashMap<String, Double> relevantWeightMap = null;
	private HashMap<String, Double> irrelevantWeightMap = null;
	private ArrayList<Document> relevantDocs = null;
	private ArrayList<Document> irrelevantDocs = null;
	private Query query = null;
	
	/**
	 * Constructor for Rocchio 
	 * @param query -> current query
	 * @param relevantDocs -> an ArrayList containing all relevant documents
	 * @param irrelevantDocs -> an ArrayList containing all irrelevant documents
	 */
	public Rocchio(Query query, 
				   ArrayList<Document> relevantDocs,
				   ArrayList<Document> irrelevantDocs) {
		this.query = query;
		this.relevantDocs = relevantDocs;
		this.irrelevantDocs = irrelevantDocs;
		termsWeights = new TreeMap<String, Double>();
		queryWeightMap = new HashMap<String, Double>();
		relevantWeightMap = new HashMap<String, Double>();
		irrelevantWeightMap = new HashMap<String, Double>();
		
		// 1. get weight of each terms for every documents
		getDocumentTermsWeights();
		
		// 2. compute all terms' weight
		computeAllTermsWeight();
	}
	
	/**
	 * Get all terms' weights among relevant docs, irrelevant docs and query
	 * 1.Compute term weights from documents. 
	 * 	 Meanwhile, compute document frequency for computing query weights
	 * 2.Compute term weights from query
	 */
	private void getDocumentTermsWeights() {
		HashMap<String, Integer> dfMap = new HashMap<String, Integer>();
		
		// first step
		for (Document doc : relevantDocs) {
			doc.getTermsWeights(relevantWeightMap);
			
			for (String queryKey : query.getTFMap().keySet()) {
				if (doc.getTFMap().containsKey(queryKey)) {
					if (!dfMap.containsKey(queryKey)) {
						dfMap.put(queryKey, 0);
					}
					dfMap.put(queryKey, dfMap.get(queryKey) + 1);
				}
			}
		}
		for (Document doc : irrelevantDocs) {
			doc.getTermsWeights(irrelevantWeightMap);
			
			for (String queryKey : query.getTFMap().keySet()) {
				if (doc.getTFMap().containsKey(queryKey)) {
					if (!dfMap.containsKey(queryKey)) {
						dfMap.put(queryKey, 0);
					}
					dfMap.put(queryKey, dfMap.get(queryKey) + 1);
				}
			}
		}
		
		// second step
		query.getTermsWeight(dfMap, queryWeightMap);
	}
	
	/**
	 * Combine all terms' weights
	 * Create one sorted map in descending order, containing term and its weight 
	 */
	private void computeAllTermsWeight() {
		for (String term : queryWeightMap.keySet()) {
			double weight = alpha * queryWeightMap.get(term);
			
			if (!termsWeights.containsKey(term)) {
				termsWeights.put(term, weight);
			} else {
				termsWeights.put(term, termsWeights.get(term) + weight);
			}
			
		}
		for (String term : relevantWeightMap.keySet()) {
			double rw = beta * relevantWeightMap.get(term);
			double irw = gamma * (irrelevantWeightMap.containsKey(term) ? irrelevantWeightMap.get(term) : 0.0);
			double weight = rw + irw;
			
			if (!termsWeights.containsKey(term)) {
				termsWeights.put(term, weight);
			} else {
				termsWeights.put(term, termsWeights.get(term) + weight);
			}
		}
	}
	
	/**
	 * Create new query according the sorted map obtained from above method
	 * @return
	 */
	public String getNewQuery() {
		StringBuilder sb = new StringBuilder();
		int limit = query.getTFMap().size();
		int newCount = 0;
		HashSet<String> newKeys = new HashSet<String>();
		
		// Sort the TreeMap by values
		TreeMap<String, Double> sortedMap = sortedByValues(termsWeights);
		
		for (String key : sortedMap.keySet()) {
			if (!query.getTFMap().containsKey(key) && newCount < 2) {
				sb.append(key + " ");
				newKeys.add(key);
				newCount++;
			} 
			if (query.getTFMap().containsKey(key) && limit > 0) {
				sb.append(key + " ");
				newKeys.add(key);
				limit--;
			} 
			
			if (limit == 0 && newCount == 2) {
				break;
			}
		}
		
		
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	// Method for sorting the TreeMap based on values
	private static <K, V extends Comparable<V>> TreeMap<K, V> 
					sortedByValues (final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			// descending order
			public int compare(K k1, K k2) {
				return map.get(k2).compareTo(map.get(k1));
			}
		};
		
		TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
}

