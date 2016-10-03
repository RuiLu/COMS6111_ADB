/**
 * Tools
 * This class is used for dealing with some text-processing work.
 * 
 * @author Rui Lu
 * @time 2016-10-2
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tools {
	private HashSet<String> stopwordSet;
	private final String filePath = "files/stopwords.txt";
	public Tools() {
		stopwordSet = getStopwordSet();
		System.out.println(stopwordSet.size());
	}
	
	/**
	 * Used to retrieve all stop words from local stopwords.txt file
	 * @return
	 */
	private HashSet<String> getStopwordSet() {
		HashSet<String> set = new HashSet<String>();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = br.readLine()) != null) {
				set.add(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return set;
	}
	
	/**
	 * Given a source string, convert it into term list, using a regular expression
	 * @param tokens - term list, which stores all terms from a given document
	 * @param source - source file, which is url, title and description from document
	 */
	public void getTerms(ArrayList<String> tokens, String source) {
		String s = "\\d+.\\d+|\\w+";
		Pattern pattern = Pattern.compile(s);  
        Matcher ma = pattern.matcher(source);  
   
        while (ma.find()){
        	String token = ma.group();
            if (!stopwordSet.contains(token)) {
            	tokens.add(token);
            }
        }	
	}
	
}
