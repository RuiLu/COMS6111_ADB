import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Tools {
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private ArrayList<ArrayList<String>> table = null;
	
	public Tools() {
		table = new ArrayList<ArrayList<String>>();
	}
	
	public ArrayList<ArrayList<String>> generateTable(String fileName) {
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;
				if (count == 1) continue;
				String[] tokens = line.split(",");
				ArrayList<String> list = new ArrayList<String>();
				for (String token : tokens) {
					if (token == null || token.equals("") || token.length() == 0) continue;
					list.add(token);
				}
				table.add(list);
			}
			if (br != null) br.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return table;
	}
	
	public void WriteOutputFile(Map<ArrayList<String>, Double> frequentItemsetsWithSupport,
			Map<String, Double> rulesetsWithConfidence,
			Double min_sup, Double min_conf) {
		try {
			pw = new PrintWriter(new FileWriter("example-run.txt"));
			
			pw.println("==Frequent itemsets (min_sup=" + 100 * min_sup + "%)");
			for (Map.Entry<ArrayList<String>, Double> me : entriesSortedByValues(frequentItemsetsWithSupport)) {
				pw.println(me.getKey() + ", " + 100 * me.getValue() + "%");
			}
			
			pw.println("\n==High-confidence association rules (min_conf=" + 100 * min_conf + "%)");
			for (Map.Entry<String, Double> me : entriesSortedByValues(rulesetsWithConfidence)) {
				pw.println(me.getKey());
			}
			
			if (pw != null) pw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public ArrayList<Rule> generateRuleset(ArrayList<Lt> allLts, double confidence) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		for (int i = 1;i < allLts.size(); i++) {
			Lt previous = allLts.get(i - 1);
			Lt now = allLts.get(i);
			for (ArrayList<String> itemset : now.itemsets) {
				double support = now.return_support(itemset);
				for(int k = 0; k < itemset.size(); k++) {
					ArrayList<String> temp = new ArrayList<String>(itemset);
					temp.remove(k);
					Double support2 = previous.return_support(temp);
					if(support / support2 >= confidence) {
						rules.add(new Rule(temp, itemset.get(k), support / support2, now.return_support(itemset)));
					}
				}
			}
		}
		return rules;
	}
	
	private <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e2.getValue().compareTo(e1.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
}
