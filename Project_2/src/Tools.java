import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

/*
 * Put functions related to set up queries to Bing here.
 * 
 */
public class Tools {
	
	private BufferedReader br = null;
	
	private final String urlPattern_1 = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a";
	private final String urlPattern_2 = "%20";
	private final String urlPattern_3 = "%27&$top=4&$format=JSON";
	
//	private String urlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a%27%s%27%20%27%s%27&$top=10&$format=JSON";
	
	private String inputUrl = null;
	private String accountKeyEnc = null;
	
	private final String rootPath = "files/root.txt";
	private final String computerPath = "files/computer.txt";
	private final String healthPath = "files/health.txt";
	private final String sportsPath = "files/sports.txt";
	
	private HashMap<String, TreeSet<String>> urlToDocMap = new HashMap<String, TreeSet<String>>();
	private HashMap<String, TreeSet<String>> catToUrlMap = new HashMap<String, TreeSet<String>>();
	private ArrayList<Category> classification = null;
	
	
	public Tools(String bingAPIKey, String inputUrl) {
		this.inputUrl = inputUrl;
		
		byte[] accountKeyBytes = Base64.encodeBase64((bingAPIKey + ":" + bingAPIKey).getBytes());
		this.accountKeyEnc = new String(accountKeyBytes);
	}
	
	/**
	 * Get a query's webTotal
	 * @param query
	 * @return
	 */
	public int getWebTotal(String query, String category) {
		int webTotal = 0;
		InputStream inputStream = null;
		
		try {
			
			String queryEnc = URLEncoder.encode(query.toString(), Charset.defaultCharset().name());
			String bingUrl = urlPattern_1 + inputUrl + urlPattern_2 + queryEnc + urlPattern_3;
			
//			System.out.println("URL: " + bingUrl);
			URL url;
			url = new URL(bingUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
			
			inputStream = (InputStream)urlConnection.getContent();
			final byte[] contentRaw = new byte[urlConnection.getContentLength()];
			inputStream.read(contentRaw);
			final String content = new String(contentRaw);
				
			final JSONObject json = new JSONObject(content);
			final JSONObject d = json.getJSONObject("d");
			final JSONArray jsonArray = d.getJSONArray("results");
			final JSONArray webJsonArray = jsonArray.getJSONObject(0).getJSONArray("Web");
			
			
			final int webJsonArrayLength = webJsonArray.length();
			for (int i = 0; i < webJsonArrayLength; i++) {
				JSONObject j = (JSONObject)webJsonArray.get(i);
				String urlString = j.getString("Url");
				
				// update catToUrlMap
				if (!catToUrlMap.containsKey(category)) {
					catToUrlMap.put(category, new TreeSet<String>());
				}
				catToUrlMap.get(category).add(urlString);
				
				// update urlToDocMap
				TreeSet<String> doc = GetWordsLynx.runLynx(urlString);
				urlToDocMap.put(urlString, doc);
				
			}
			
			final JSONObject meta = jsonArray.getJSONObject(0);
			final String webTotalString = meta.getString("WebTotal"); 
			
			webTotal = Integer.parseInt(webTotalString);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return webTotal;
	}
	
	/**
	 * Root
	 * 	- Computers
	 * 		- Hardware
	 * 		- Programming
	 * 	- Health
	 * 		- Fitness
	 * 		- Diseases
	 * 	- Sports
	 * 		- Basketball
	 * 		- Soccer
	 * @return Organized Category
	 */
	public Category setupCategory() {
		Category root = null;
		
		try {
			String line = null;
			
			// Third level -> soccer + basketball from sports
			br = new BufferedReader(new FileReader(sportsPath));
			
			Category soccer = new Category("Soccer", new ArrayList<String>());
			Category basketball = new Category("Basketball", new ArrayList<String>());
			
			while ((line = br.readLine()) != null) {
				int firstSpace = line.indexOf(" ");
				String category = line.substring(0, firstSpace);
				String query = line.substring(firstSpace + 1);
				
				if (category.toLowerCase().equals("soccer")) {
					soccer.addQuery(query);
				} else if (category.toLowerCase().equals("basketball")) {
					basketball.addQuery(query);
				}
			}
			
			this.close();
			
			// Third level -> diseases + fitness from health
			br = new BufferedReader(new FileReader(healthPath));
			
			Category diseases = new Category("Diseases", new ArrayList<String>());
			Category fitness = new Category("Fitness", new ArrayList<String>());
			
			while ((line = br.readLine()) != null) {
				int firstSpace = line.indexOf(" ");
				String category = line.substring(0, firstSpace);
				String query = line.substring(firstSpace + 1);
				
				if (category.toLowerCase().equals("diseases")) {
					diseases.addQuery(query);
				} else if (category.toLowerCase().equals("fitness")) {
					fitness.addQuery(query);
				}
			}
			
			this.close();
			
			// Third level -> hardware + programming from computer
			br = new BufferedReader(new FileReader(computerPath));
						
			Category hardware = new Category("Hardware", new ArrayList<String>());
			Category programming = new Category("Programming", new ArrayList<String>());
						
			while ((line = br.readLine()) != null) {
				int firstSpace = line.indexOf(" ");
				String category = line.substring(0, firstSpace);
				String query = line.substring(firstSpace + 1);
					
				if (category.toLowerCase().equals("hardware")) {
					hardware.addQuery(query);
				} else if (category.toLowerCase().equals("programming")) {
					programming.addQuery(query);
				}
			}	
			
			this.close();
			
			// Second level -> computer + health + sports from root
			br = new BufferedReader(new FileReader(rootPath));
			
			Category computers = new Category("Computers", new ArrayList<String>());
			Category sports = new Category("Sports", new ArrayList<String>());
			Category health = new Category("Health", new ArrayList<String>());
			
			while ((line = br.readLine()) != null) {
				int firstSpace = line.indexOf(" ");
				String category = line.substring(0, firstSpace);
				String query = line.substring(firstSpace + 1);
				
				if (category.toLowerCase().equals("computers")) {
					computers.addQuery(query);
				} else if (category.toLowerCase().equals("health")) {
					health.addQuery(query);
				} else if (category.toLowerCase().equals("sports")) {
					sports.addQuery(query);
				}
			}
			
			this.close();
			
			// First level -> root
			root = new Category("Root");
			
			// Add third-level categories to second-level category
			computers.addCategory(hardware);
			computers.addCategory(programming);
			health.addCategory(diseases);
			health.addCategory(fitness);
			sports.addCategory(soccer);
			sports.addCategory(basketball);
			
			// Add second-level categories to first-level category
			root.addCategory(computers);
			root.addCategory(health);
			root.addCategory(sports);
		
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		
		return root;
	}
	
	/**
	 *	"current" represents the current category we are looking at.
	 *  It should be root at the beginning, and we will look deeper into it
	 *  until it has no sub_categories or does not meet the threshold. 
	 * @param t_ec -> the threshold of coverage
	 * @param t_es -> the threshold of specificity
	 * @param root -> root category
	 * @return
	 */
	public String QProb(Integer t_ec, Double t_es, Category root) {
		
		ArrayList<Category> pending = new ArrayList<Category>();
		pending.add(root);
		String res = "";
		classification = new ArrayList<Category>();
		
		while (pending.size() != 0) {
			ArrayList<Category> next = new ArrayList<Category>();
			for (Category current : pending) {
				Integer tot = 0;
				classification = new ArrayList<Category>();
				String categoryName = current.getName();
				for (Category sub : current.getSubCategories()) {
					Integer ecoverage = 0;
					
					for (String q : sub.getQuries()) {
						Integer f = getWebTotal(q, categoryName);
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
			pending=new ArrayList<Category>(next);
		}
		
		return res;
	}
	
	public void content_summary() throws IOException {
		for(Category cat:classification) {
			/*
			 * Here we will ignore leaves
			 */
			if(cat.getSubCategories().size()>0){
				/*
				 * Here we should have some code to open a file.
				 */
				Set<String> url_set = cat.url_set(catToUrlMap);
				Set<String> words = new TreeSet<String>();
				for(String url:url_set) {
					words.addAll(urlToDocMap.get(url));
				}
				
				String filePath = cat.getName() + "-" + inputUrl + ".txt";
				FileWriter fw = new FileWriter(filePath, true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				
				for(String word:words) {
					Integer count=0;
					for(String url:url_set) {
						if(urlToDocMap.get(url).contains(word)) {
							count++;
						}
					}
					/*
					 * Here should be some code to write the <word,count> pair;
					 */
					String output = word + "#" + count;
					pw.println(output);
				}
				
				/*
				 * Here we should have some code to close that file.
				 */
				if (pw != null) pw.close();
				if (fw != null) fw.close();
			}
			
		}
	}
	
	public void close() {
		try {
			if (br != null) br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
