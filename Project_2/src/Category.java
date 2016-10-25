import java.util.ArrayList;


public class Category {
	public String name;
	public ArrayList<String> query;
	public ArrayList<Category> subCategory;
	public Integer ecoverage=0;
	public Double especificity=1.0;
	public Category(String n) {
		name=n;
		query=new ArrayList<String>();
		subCategory=new ArrayList<Category>();
	}
	public Category(String n, ArrayList<String> keywords) {
		name=n;
		query=keywords;
		subCategory=new ArrayList<Category>();
	}
}
