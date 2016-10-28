import java.util.ArrayList;


public class Category {
	private String name = null;
	private String path = null;
	private ArrayList<String> queries = null;
	private ArrayList<Category> subCategories = null;
	private Integer eCoverage = 0;
	private Double eSpecificity = 1.0;
	
	public Category(String n) {
		this.name = n;
		this.queries = new ArrayList<String>();
		this.subCategories = new ArrayList<Category>();
	}
	
	public Category(String n, ArrayList<String> keywords) {
		this.name = n;
		this.queries = keywords;
		this.subCategories = new ArrayList<Category>();
	}
	
	public void printCategory() {
		for(String query : queries) {
			System.out.println(path + " " + query);
		}
		System.out.println();
		for(Category sub : subCategories) {
			sub.printCategory();
		}
	}
	
	public void update_path(String p) {
		path=p+name;
		for(Category sub:subCategories) {
			sub.update_path(path+"/");
		}
	}

	public void addQuery(String query) {
		this.queries.add(query);
	}
	
	public ArrayList<String> getQuries() {
		return this.queries;
	}
	
	public void addCategory(Category subCategory) {
		this.subCategories.add(subCategory);
	}
	
	public ArrayList<Category> getSubCategories() {
		return this.subCategories;
	}
	
	public Integer geteCoverage() {
		return eCoverage;
	}

	public void seteCoverage(Integer eCoverage) {
		this.eCoverage = eCoverage;
	}

	public Double geteSpecificity() {
		return eSpecificity;
	}

	public void seteSpecificity(Double eSpecificity) {
		this.eSpecificity = eSpecificity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
