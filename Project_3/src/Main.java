import java.util.ArrayList;

public class Main {

	private static String fileName = null;
	private static double min_sup = 0.0;
	private static double min_conf = 0.0;
	
	private static Tools tools = null;
	private static Database database = null;
	private static Lt lt = null;
	
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
		
		fileName = "Water_Quality_complaints.csv";
	
		tools = new Tools();
		ArrayList<ArrayList<String>> table = tools.generateTable(fileName);
		database = new Database(table, min_sup, min_conf);
	}

}
