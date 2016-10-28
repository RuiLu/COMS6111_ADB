import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

/*
 * Put functions related to set up queries to Bing here.
 * 
 */
public class Tools {
	
	private InputStream inputStream = null;
	private BufferedReader br = null;
	
	private final String urlPattern_1 = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a";
	private final String urlPattern_2 = "%20";
	private final String urlPattern_3 = "%27&$top=10&$format=JSON";
	
	private String inputUrl = null;
	private String accountKeyEnc = null;
	
	private final String rootPath = "files/root.txt";
	private final String computerPath = "files/computer.txt";
	private final String healthPath = "files/health.txt";
	private final String sportsPath = "files/sports.txt";
	
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
	public int getWebTotal(String query) {
		int webTotal = 0;
		
		try {
			String bingUrl = urlPattern_1 + inputUrl + urlPattern_2 + query + urlPattern_3;
			System.out.println("URL: " + bingUrl);
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
				
			final JSONObject meta = jsonArray.getJSONObject(0);
			final String webTotalString = meta.getString("WebTotal"); 
			
			webTotal = Integer.parseInt(webTotalString);
			
			System.out.println("from tools:" + webTotal);
			
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
			
			Category soccer = new Category("soccer", new ArrayList<String>());
			Category basketball = new Category("basketball", new ArrayList<String>());
			
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
			
			Category diseases = new Category("diseases", new ArrayList<String>());
			Category fitness = new Category("fitness", new ArrayList<String>());
			
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
						
			Category hardware = new Category("hardware", new ArrayList<String>());
			Category programming = new Category("programming", new ArrayList<String>());
						
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
			
			Category computers = new Category("computers", new ArrayList<String>());
			Category sports = new Category("sports", new ArrayList<String>());
			Category health = new Category("health", new ArrayList<String>());
			
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
			root = new Category("root");
			
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
	
	public void close() {
		try {
			if (br != null) br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static Category setup_category() {
		/*
		Root 
			Computers 
				Hardware 
				Programming 
			Health 
				Fitness 
				Diseases 
			Sports 
				Basketball 
				Soccer
		 */ 

		//Third level categories
		Category hardware=new Category("Hardware",
				new ArrayList<String>(
						Arrays.asList(
								"bios", "motherboard", "board fsb", "board overclocking",
								"fsb overclocking", "bios controller ide", "cables drive floppy"
						)
				)
		);
		Category programming=new Category("Programming",
				new ArrayList<String>(
						Arrays.asList(
								"actionlistener", "algorithms", "alias", "alloc", "ansi",
								"api", "applet", "argument", "array", "binary", "boolean",
								"borland", "char", "class", "code", "compile", "compiler",
								"component", "container", "controls", "cpan", "java", "perl"
						)
				)
		);
		Category fitness=new Category("Fitness",
				new ArrayList<String>(
						Arrays.asList(
								"aerobic", "fat", "fitness", "walking", "workout", "acid diets",
								"bodybuilding protein", "calories protein", "calories weight",
								"challenge walk", "dairy milk", "eating protein", "eating weight",
								"exercise protein", "exercise weight"
						)
				)
		);
		Category diseases=new Category("Diseases",
				new ArrayList<String>(
						Arrays.asList(
								"aids", "cancer", "dental", "diabetes", "hiv", "cardiology",
								"aspirin cardiologist", "aspirin cholesterol", "blood heart",
								"blood insulin", "cholesterol doctor", "cholesterol lipitor",
								"heart surgery", "radiation treatment"
						)
				)
		);
		Category basketball=new Category("Basketball",
				new ArrayList<String>(
						Arrays.asList(
								"nba", "pacers", "kobe", "laker", "shaq", "blazers", "knicks",
								"sabonis", "shaquille", "laettner", "wnba", "rebounds", "dunks"
						)
				)
		);
		Category soccer=new Category("Soccer",
				new ArrayList<String>(
						Arrays.asList(
								"uefa", "leeds", "bayern","bundesliga","premiership","lazio",
								"mls", "hooliganism", "juventus", "liverpool", "fifa"
						)
				)
		);
		
		//Second level categories
		Category sports=new Category("Sports",
				new ArrayList<String>(
						Arrays.asList(
								"laker", "ncaa", "pacers", "soccer", "teams", "wnba", "nba", "avg league",
								"avg nba", "ball league", "ball referee", "ball team", "blazers game",
								"championship team", "club league", "fans football", "game league"
						)
				)
		);
		sports.addCategory(basketball);
		sports.addCategory(soccer);
		
		Category health=new Category("Health",
				new ArrayList<String>(
						Arrays.asList(
								"acupuncture", "aerobic", "aerobics", "aids", "cancer", "cardiology",
								"cholesterol", "diabetes", "diet", "fitness", "hiv", "insulin", "nurse",
								"squats", "treadmill", "walkers", "calories fat", "carb carbs", "doctor health",
								"doctor medical", "eating fat", "fat muscle", "health medicine", "health nutritional",
								"hospital medical", "hospital patients", "medical patient", "medical treatment",
								"patients treatment"
						)
				)
		);
		health.addCategory(fitness);
		health.addCategory(diseases);
		
		Category computers=new Category("Computers",
				new ArrayList<String>(
						Arrays.asList(
								"cpu", "java", "module", "multimedia", "perl", "vb", "agp card",
								"application windows", "applet code", "array code", "audio file",
								"avi file", "bios", "buffer code", "bytes code", "shareware",
								"card drivers", "card graphics", "card pc", "pc windows"
						)
				)
		);
		computers.addCategory(hardware);
		computers.addCategory(programming);

		//Root
		Category root=new Category("Root");
		root.addCategory(sports);
		root.addCategory(health);
		root.addCategory(computers);
		
		//update path
		root.update_path("");
		return root;
	}
}