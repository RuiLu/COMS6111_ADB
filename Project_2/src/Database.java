import java.util.ArrayList;


public class Database {
	public String url;	
	Integer t_ec;
	Double t_es;
	Database(String database_link) {
		url=database_link;

	}
	
	public String QProb(Integer ec, Double es, Category root, String  APIkey) {
		t_ec=ec;
		t_es=es;
		/*
		 * "current" represents the current category we are looking at.
		 * It should be root at the beginning, and we will look deeper into it
		 * until it has no sub_categories or does not meet the threshold. 
		 */
		ArrayList<Category> pending=new ArrayList<Category>();
		pending.add(root);
		String res="";
		
		while (pending.size()!=0) {
			ArrayList<Category> next = new ArrayList<Category>();
			for (Category current : pending) {
				Integer tot=0;
				for (Category sub : current.getSubCategories()) {
					Integer ecoverage = 0;
					for (String q : sub.getQuries()) {
						Integer f = 0;	//Needs a function;
						ecoverage += f;
					}
					sub.seteCoverage(ecoverage);
					tot += ecoverage;
				}
				Integer available_sub=0;
				for (Category sub : current.getSubCategories()) {
					sub.seteSpecificity(current.geteSpecificity() * sub.geteCoverage() / tot);
					if ((sub.geteCoverage() >= t_ec) && (sub.geteSpecificity() > t_es)) {
						next.add(sub);
						available_sub++;
					}
				}
				if (available_sub == 0) {
					if (res.equals("")) res = current.getPath();
					else res = res + " AND " + current.getPath();
				}
			}
		}
		
		return res;
	}
}
