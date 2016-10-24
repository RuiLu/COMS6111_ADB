
public class Database {
	public String url;	
	Integer t_ec;
	Double t_es;
	Database(String database_link) {
		url=database_link;

	}
	
	public String QProb(Integer ec, Double es, Category root) {
		t_ec=ec;
		t_es=es;
		/*
		 * "current" represents the current category we are looking at.
		 * It should be root at the beginning, and we will look deeper into it
		 * until it has no sub_categories or does not meet the threshold. 
		 */
		Category current=root;
		String res=current.name;
		
		
		return res;
	}
}
