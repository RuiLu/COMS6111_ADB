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
