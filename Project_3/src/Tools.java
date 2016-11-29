import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Tools {
	private BufferedReader br = null;
	private ArrayList<ArrayList<String>> table = null;
	
	public Tools() {
		table = new ArrayList<ArrayList<String>>();
	}
	
	public ArrayList<ArrayList<String>> generateTable(String fileName) {
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				ArrayList<String> list = new ArrayList<String>();
				for (String token : tokens) {
					list.add(token);
				}
				table.add(list);
			}
			close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return table;
	}
	
	public void generateItemset() {
		
	}
	
	public void generateRuleset() {
		
	}
	
	private void close() {
		try {
			if (br != null) br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Rule> generateRuleset(ArrayList<Lt> allLts, double confidence) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		for (int i=1;i<allLts.size();i++) {
			Lt previous=allLts.get(i-1);
			Lt now=allLts.get(i);
			for(ArrayList<String> itemset:now.itemsets){
				double support=now.support.get(i);
				for(int k=0;k<itemset.size();k++) {
					ArrayList<String> temp=new ArrayList<String>(itemset);
					temp.remove(k);
					double support2=previous.return_support(temp);
					if(support/support2>confidence) {
						rules.add(new Rule(temp,itemset.get(k),support/support2));
					}
				}
			}
		}
		return rules;
	}
}
