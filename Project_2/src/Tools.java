import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

/*
 * Put functions related to set up queries to Bing here.
 * 
 */
public class Tools {
	
	private static InputStream inputStream = null;
	
	private static final String urlPattern_1 = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a";
	private static final String urlPattern_2 = "%20";
	private static final String urlPattern_3 = "%27&$top=10&$format=JSON";
	
	private static String bingAPIKey = null;
	private static String inputUrl = null;
	
	public Tools(String bingAPIKey, String inputUrl) {
		this.bingAPIKey = bingAPIKey;
		this.inputUrl = inputUrl;
	}
	
	public int getWebTotal(String query) {
		int webTotal = 0;
		
		byte[] accountKeyBytes = Base64.encodeBase64((bingAPIKey + ":" + bingAPIKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);
		
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
		sports.subCategory.add(basketball);
		sports.subCategory.add(soccer);
		
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
		health.subCategory.add(fitness);
		health.subCategory.add(diseases);
		
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
		computers.subCategory.add(hardware);
		computers.subCategory.add(programming);

		//Root
		Category root=new Category("Root");
		root.subCategory.add(sports);
		root.subCategory.add(health);
		root.subCategory.add(computers);
		
		//update path
		root.update_path("");
		return root;
	}
}
