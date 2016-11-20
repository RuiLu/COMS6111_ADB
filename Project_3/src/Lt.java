import java.util.ArrayList;


public class Lt {
	ArrayList<ArrayList<String>> itemsets;
	ArrayList<Double> support;
	public Lt(ArrayList<ArrayList<String>> itemsets, ArrayList<Double> support) {
		this.itemsets=new ArrayList<ArrayList<String>>(itemsets);
		this.support=new ArrayList<Double>(support);
	}
	
	public ArrayList<ArrayList<String>> generate_cancidates() {
		ArrayList<ArrayList<String>> res=new ArrayList<ArrayList<String>>();
		for(ArrayList<String> item1:itemsets)
			for(ArrayList<String> item2:itemsets) {
				boolean pass=true;
				int n=item1.size()-1;
				if(item1.get(n).compareTo(item2.get(n))<0) {
					for(int i=0;i<n;i++) {
						if(item1.get(i).compareTo(item2.get(i))!=0) {
							pass=false;
							break;
						}
					}
				}
				if(pass) {
					ArrayList<String> temp=new ArrayList<String>(item1);
					temp.add(item2.get(n));
					if(validate(temp)) {
						res.add(temp);
					}
				}
			}
		
		return res;
	}
	
	private boolean validate(ArrayList<String> s) {
		boolean res=true;
		for(int i=0;i<s.size();i++) {
			ArrayList<String> temp=new ArrayList<String>(s);
			temp.remove(i);
			boolean have=false;
			for(ArrayList<String> item:itemsets){
				boolean equal=true;
				for(int j=0;j<item.size();j++)
					if(!temp.get(j).equals(item.get(j))) {
						equal=false;
						break;
					}
				if(equal) have=true;
			}
			if(!have) {
				res=false;
				break;
			}
		}
		
		return res;
	}

}
