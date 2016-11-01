import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {

		final String bingAPIKey = "kb6M6x15DP+nno7y8uWF1RXDitysb9EZb1Bif/kLod0";
		final String inputUrl = "hardwarecentral.com";
		final int tec = 100;
		final double tes = 0.6;		
			
		Tools tools = new Tools(bingAPIKey, inputUrl);
		
		Category root = tools.setupCategory();
		root.updatePath("");
		
		try {
			tools.content_summary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String res = tools.QProb(tec, tes, root);
		System.out.println(inputUrl + ": "+ res);
		
	}

}
