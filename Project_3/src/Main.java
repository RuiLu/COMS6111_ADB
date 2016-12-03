import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {

	private static String fileName = null;
	private static double min_sup = 0.0;
	private static double min_conf = 0.0;
	
	private static Tools tools = null;
	private static Database database = null;
	private static Lt lt = null;
	
	private static ArrayList<ArrayList<String>> originalCandidte = null;
	private static ArrayList<Lt> allLts = null;
	private static Map<ArrayList<String>, Double> frequentItemsetsWithSupport;
	private static Map<String, Double> rulesetsWithConfidence;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3)  {
			System.out.println("How to run this program:");
			System.out.println("./run.sh <csv file> <min_sup> <min_conf>");
			return;
		}
		
		fileName = args[0].trim();
		min_sup = Double.parseDouble(args[1].trim());
		min_conf = Double.parseDouble(args[2].trim());
		
		allLts = new ArrayList<Lt>();
		frequentItemsetsWithSupport = new HashMap<ArrayList<String>, Double>();
		rulesetsWithConfidence = new HashMap<String, Double>();
	
		tools = new Tools();
		ArrayList<ArrayList<String>> table = tools.generateTable(fileName);
		database = new Database(table, min_sup);
		originalCandidte = database.getOriginalCandidate();
		
		lt = database.generate_Lt(originalCandidte);

		System.out.println("==Frequent itemsets (min_sup=" + 100 * min_sup + "%)");
		
		while (true) {
			if (lt.itemsets.size() == 0) break;
			allLts.add(lt);
			for (int i = 0; i < lt.itemsets.size(); i++) {
				frequentItemsetsWithSupport.put(lt.itemsets.get(i), lt.support.get(i));
			}
			ArrayList<ArrayList<String>> c = lt.generate_cancidates();
			lt = database.generate_Lt(c);
		}
		
		for (Map.Entry<ArrayList<String>, Double> me : entriesSortedByValues(frequentItemsetsWithSupport)) {
			System.out.println(me.getKey() + ", " + 100 * me.getValue() + "%");
		}
		
		System.out.println("\n==High-confidence association rules (min_conf=" + 100 * min_conf + "%)");
		
		ArrayList<Rule> rules = tools.generateRuleset(allLts, min_conf);
		for(Rule rule : rules) {
			StringBuilder sb = new StringBuilder();
			sb.append(rule.get_left());
			sb.append(" => ");
			sb.append(rule.get_right());
			sb.append(" ");
			sb.append("(Conf: "+ 100 * rule.get_confidence() + "%, Supp:" + 100 * rule.get_support() + "%)");
			rulesetsWithConfidence.put(sb.toString(), rule.get_confidence());
		}
		
		for (Map.Entry<String, Double> me : entriesSortedByValues(rulesetsWithConfidence)) {
			System.out.println(me.getKey());
		}
		
		tools.WriteOutputFile(frequentItemsetsWithSupport, rulesetsWithConfidence, min_sup, min_conf);
	}
	
	private static <K,V extends Comparable<? super V>>
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
	
//	private ArrayList<ArrayList<String>> generateTestTable() {
//		ArrayList<ArrayList<String>> testTable = new ArrayList<ArrayList<String>>();
//		ArrayList<String> a1 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "INK", "DIARY", "SOAP"}));
//		ArrayList<String> a2 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "INK", "DIARY"}));
//		ArrayList<String> a3 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "DIARY"}));
//		ArrayList<String> a4 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "INK", "SOAP"}));
//		testTable.add(a1);
//		testTable.add(a2);
//		testTable.add(a3);
//		testTable.add(a4);
//		return testTable;
//	}
}
