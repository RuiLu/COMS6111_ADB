import java.util.ArrayList;


public class Category {
	public String name;
	public ArrayList<String> query;
	public ArrayList<Category> subCategory;
	public Category(String n) {
		name=n;
	}
}
