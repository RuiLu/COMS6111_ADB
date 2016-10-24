import java.util.ArrayList;
import java.util.Arrays;

/*
 * Put functions related to set up queries to Bing here.
 * 
 */
public class Tools {
	public Category setup_category() {
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
		return root;
	}
}
