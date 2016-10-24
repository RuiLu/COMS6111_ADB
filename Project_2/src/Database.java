
public class Database {
	public String name;	
	Integer t_ec;
	Double t_es;
	Database(String database_name) {
		name=database_name;

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
		
		
		return current.name;
	}
}
