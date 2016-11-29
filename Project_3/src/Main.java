import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	private static String fileName = null;
	private static double min_sup = 0.0;
	private static double min_conf = 0.0;
	
	private static Tools tools = null;
	private static Database database = null;
	private static Lt lt = null;
	private static ArrayList<ArrayList<String>> originalCandidte = null;
	private static ArrayList<Lt> allLts = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		if (args.length != 3)  {
//			System.out.println("How to run this program:");
//			System.out.println("./run.sh <csv file> <min_sup> <min_conf>");
//			return;
//		}
//		
//		fileName = args[0].trim();
//		min_sup = Double.parseDouble(args[1].trim());
//		min_conf = Double.parseDouble(args[2].trim());
		
		allLts = new ArrayList<Lt>();
		
		ArrayList<ArrayList<String>> testTable = new ArrayList<ArrayList<String>>();
		ArrayList<String> a1 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "APPLE", "PINEAPPLE"}));
		ArrayList<String> a2 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "AB"}));
		ArrayList<String> a3 = new ArrayList<String>(Arrays.asList(new String[]{"PEN", "APPLE", "PINEAPPLE", "CD"}));
		ArrayList<String> a4 = new ArrayList<String>(Arrays.asList(new String[]{"APPLE", "WATER"}));
		testTable.add(a1);
		testTable.add(a2);
		testTable.add(a3);
		testTable.add(a4);
		
		fileName = "Water_Quality_complaints.csv";
	
		tools = new Tools();
		ArrayList<ArrayList<String>> table = tools.generateTable(fileName);
		database = new Database(testTable, 0.5, min_conf);
		originalCandidte = database.getOriginalCandidate();
	
		lt = database.generate_Lt(originalCandidte);
		
		while (true) {
			if (lt.itemsets.size() == 0) break;
			allLts.add(lt);
//			System.out.println(lt.itemsets);
			ArrayList<ArrayList<String>> c = lt.generate_cancidates();
			lt = database.generate_Lt(c);
		}
		
		ArrayList<Rule> rules=tools.generateRuleset(allLts, 0.8);
		for(Rule r:rules) {
			System.out.print(r.get_left());
			System.out.print(" ");
			System.out.print(r.get_right());
			System.out.print(" ");
			System.out.println(r.get_confidence());
		}
		
	}

}
