import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

public class Main {
	
	private static String bingAPIKey;
	private static int precision;
	private static String query;
	private final static String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27gates%27&$top=10&$format=Atom";
	
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println("Correct format: " + args.length);

			return;
		}
		
		bingAPIKey = args[0];
		precision = (int)(Double.parseDouble(args[1]) * 10);
		query = args[2];
		
//		Scanner sc = new Scanner(System.in);
		
		byte[] accountKeyBytes = Base64.encodeBase64((bingAPIKey + ":" + bingAPIKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);
		
		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
		
		InputStream inputStream = (InputStream)urlConnection.getContent();
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		String content = new String(contentRaw);
		
		System.out.println(contentRaw);
	}
}

