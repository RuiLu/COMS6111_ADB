import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Scanner;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.codec.binary.Base64;


public class Main {
	
	private static InputStream inputStream = null;
	
	public static void main(String[] args) {

		final String bingAPIKey = "kb6M6x15DP+nno7y8uWF1RXDitysb9EZb1Bif/kLod0";
		final String inputUrl = "fifa.com";
		final String query = "chelsea";
		final int tec = 100;
		final double tes = 0.6;
				
		final String urlPattern_1 = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a";
		final String urlPattern_2 = "%20";
		final String urlPattern_3 = "%27&$top=10&$format=JSON";
			
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
			final String webTotal = meta.getString("WebTotal"); 
				
			System.out.println(webTotal);
				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
