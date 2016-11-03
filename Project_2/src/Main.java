import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		
		if (args.length != 4) {
			System.out.println("How to run this program:");
			System.out.println("./run.sh <BingAccountKey> <t_es> <t_ec> <host>");
			return;
		}

		final String bingAPIKey = args[0];
		final double t_es = Double.parseDouble(args[1]);
		final int t_ec = Integer.parseInt(args[2]);
		final String inputUrl = args[3];

		System.out.println("===============================");
		System.out.println("key:\t" + bingAPIKey);
		System.out.println("t_es:\t" + t_es);
		System.out.println("t_ec:\t" + t_ec);
		System.out.println("host:\t" + inputUrl);
		System.out.println("===============================\n");

		/*
		final String bingAPIKey = "";
		final String inputUrl = "hardwarecentral.com";
		final int tec = 100;
		final double tes = 0.6;		
		*/	
		Tools tools = new Tools(bingAPIKey, inputUrl);
		
		Category root = tools.setupCategory();
		root.updatePath("");
		
		String res = tools.QProb(t_ec, t_es, root);
		System.out.println("\n" + inputUrl + ": "+ res);
		
		try {
			tools.content_summary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
