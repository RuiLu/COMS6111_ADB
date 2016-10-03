/**
 * Main function.
 * Including a while-loop to control user input and retrieved results.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Scanner;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

public class Main {
	
	private static InputStream inputStream = null;
	private static Scanner sc = null;
	
	private static FileWriter fw = null;
	private static PrintWriter pw = null;
	private final static String filePath = "transcript.txt";
	
	private static Query query;
	private static Tools tools; 
	
	public static void main(String[] args) throws IOException {

//		for (String str : args) {
//			System.out.println(str);
//		}
//		if (args.length < 4) {
//			System.out.println("Correct format: ");
//			return;
//		}
		
		sc = new Scanner(System.in);
		query = new Query("brin".toLowerCase());
		tools = new Tools();
		
		int roundCounter = 0;
		
		final String bingAPIKey = "kb6M6x15DP+nno7y8uWF1RXDitysb9EZb1Bif/kLod0";
		final int precision = 9;
		final String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$top=10&$format=Json";
		
		// Print basic search information
		System.out.println("Parameters");
		System.out.println("Client key  = " + bingAPIKey);
		System.out.println("Query       = " + query.toString());
		System.out.println("Precision   = " + ((double)precision / 10.0));
		
		while (true) {
			int relevantCount = 0;
			ArrayList<Document> relevantDocs = new ArrayList<>();
			ArrayList<Document> irrelevantDocs = new ArrayList<>(); 
			
			String queryEnc = URLEncoder.encode(query.toString(), Charset.defaultCharset().name());
			String bingUrl = String.format(bingUrlPattern, queryEnc); 
			
			System.out.println("URL: " + bingUrl);
			
			byte[] accountKeyBytes = Base64.encodeBase64((bingAPIKey + ":" + bingAPIKey).getBytes());
			String accountKeyEnc = new String(accountKeyBytes);
			
			URL url = new URL(bingUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
			
			try {
				// open transcript.txt and record results
				fw = new FileWriter(filePath, true);
				pw = new PrintWriter(new BufferedWriter(fw));
				
				pw.println("=================================");
				pw.println("Round " + (++roundCounter));
				pw.println("QUERY " + query.toString()  + "\n");
				
				inputStream = (InputStream)urlConnection.getContent();
				final byte[] contentRaw = new byte[urlConnection.getContentLength()];
				inputStream.read(contentRaw);
				final String content = new String(contentRaw);
				
				final JSONObject json = new JSONObject(content);
				final JSONObject d = json.getJSONObject("d");
				final JSONArray jsonArray = d.getJSONArray("results");
				final int jsonArrayLength = jsonArray.length();
				
				// the length of returned results is less than 10, stop
				if (jsonArrayLength < 10) {
					System.out.println("Total number of results : " + jsonArrayLength);
					System.out.println("The length of returned results is less than 10, stop.");
					break;
				} else if (jsonArrayLength == 10) {
					System.out.println("Total number of results : " + jsonArrayLength);
					System.out.println("Bing Search Results:");
					System.out.println("=================================");
				}
				
				// start relevant feedback
				for (int i = 0; i < jsonArrayLength; i++) {
					final JSONObject object = jsonArray.getJSONObject(i);
					String URL = object.getString("Url");
					String title = object.getString("Title");
					String description = object.getString("Description");
					ArrayList<String> tokens = new ArrayList<>();
					
					// tools.getTerms(tokens, URL.toLowerCase());
					tools.getTerms(tokens, title.toLowerCase());
					tools.getTerms(tokens, description.toLowerCase());
					
					Document document = new Document(tokens);
					
					System.out.println("Result " + (i + 1));
					System.out.println("[");
					System.out.println("URL:" + URL);
					System.out.println("Title:" + title);
					System.out.println("Description:" + description);
					System.out.println("]\n");
					System.out.print("Relevant (Y/N)? ");
					String reply = sc.nextLine();
					if (reply.equals("Y") || reply.equals("y")) {
						relevantCount++;
						relevantDocs.add(document);
					} else if (reply.equals("N") || reply.equals("n")) {
						irrelevantDocs.add(document);
					}
					System.out.println();
					
					pw.println("Result " + (i + 1));
					pw.println("Relevant: " + (reply.toLowerCase().equals("y") ? "YES" : "NO"));
					pw.println("[");
					pw.println("URL        : " + URL);
					pw.println("Title      : " + title);
					pw.println("Description: " + description);
					pw.println("]\n");
					pw.println();
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				pw.println("PRECISION:" + ((double)relevantCount / 10.0));
				
				if (inputStream != null) inputStream.close();
				if (pw != null) pw.close();
				if (fw != null) fw.close();
			}
		
			if (relevantCount >= precision) {
				System.out.println("=================================");
				System.out.println("Feedback Summary:");
				System.out.println("Query : " + query.toString());
				System.out.println("Precision : " + ((double)relevantCount / 10.0));
				System.out.println("Desired precision reached, done!\nSearch finished.");
				break;
			} else if (relevantCount == 0) {
				System.out.println("=================================");
				System.out.println("Feedback Summary:");
				System.out.println("Query : " + query.toString());
				System.out.println("Precision : " + ((double)relevantCount / 10.0));
				System.out.println("Precision is 0.0, stop searcing.");
				break;
			}
			
			System.out.println("=================================");
			System.out.println("Feedback Summary:");
			System.out.println("Query : " + query.toString());
			System.out.println("Precision : " + ((double)relevantCount / 10.0));
			System.out.println("Still below the desired precision of " + ((double)precision / 10.0));
			System.out.println();
			
			// computing terms weights here
			Rocchio rocchio = new Rocchio(query, relevantDocs, irrelevantDocs);
			String newQuery = rocchio.getNewQuery();
			query = new Query(newQuery);
				
			System.out.println("New Parameters:");
			System.out.println("Client key  = " + bingAPIKey);
			System.out.println("Query       = " + query.toString());
			System.out.println("Precision   = " + ((double)precision / 10.0));
		}
		
		if (sc != null) sc.close();
	}
}
