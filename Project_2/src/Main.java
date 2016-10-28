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
	
	public static void main(String[] args) {

		final String bingAPIKey = "kb6M6x15DP+nno7y8uWF1RXDitysb9EZb1Bif/kLod0";
		final String inputUrl = "fifa.com";
		final String query = "chelsea";
		final int tec = 100;
		final double tes = 0.6;		
			
		Tools tools = new Tools(bingAPIKey, inputUrl);
		
		int webTotal = tools.getWebTotal("chelsea");
		System.out.println("from main:" + webTotal);
		
	}

}
