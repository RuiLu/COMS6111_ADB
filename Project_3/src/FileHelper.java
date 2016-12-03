import java.io.*;
import java.util.*;

public class FileHelper {
	
	private static BufferedReader br = null;
	private static PrintWriter pw = null;
	private static final char COMMA_DELIMITER = ',';
//	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "Start Time, Stop Time, Start Station, Stop Station, Birth Year, Gender";
//	private static final String FILE_HEADER = "Created Date,Descriptor,Incident Zip,City";
//	private static final String FILE_HEADER = "Created Date,Descriptor,Area";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			br = new BufferedReader(new FileReader("201601-citibike-tripdata.csv"));
			pw = new PrintWriter(new FileWriter("INTEGRATED-DATASET.csv"));
			String line = null;
			pw.println(FILE_HEADER);
			int count = 0;
			
			while ((line = br.readLine()) != null) {
				count++;
				if (count == 1) continue;
				if (count == 60690) break;
				String[] tokens = line.split(",");
				StringBuilder sb = new StringBuilder();
				
				String duration = processTime(tokens[0].trim());
				if (duration.equals("Duration: >=4h")) continue;
				sb.append(duration + COMMA_DELIMITER);
				String startTime = "StartTime: " + processDate(tokens[1].trim());
				sb.append(startTime + COMMA_DELIMITER);
//				String stopTime = "StopTime: " + processDate(tokens[2].trim());
//				sb.append(stopTime + COMMA_DELIMITER);
				String startStation = "StartStation: " + tokens[3].trim() + "-" + tokens[4].trim();
				sb.append(startStation + COMMA_DELIMITER);
				String stopStation = "StopStation: " + tokens[7].trim() + "-" + tokens[8].trim();
				sb.append(stopStation + COMMA_DELIMITER);
				String age = processAge(tokens[13].trim());
				sb.append(age + COMMA_DELIMITER);
				String gender = processGender(tokens[14].trim());
				sb.append(gender);
				
				pw.println(sb.toString());
			}
			
//			while ((line = br.readLine()) != null) {
//				count++;
//				if (count == 1) continue;
//				String[] tokens = line.split(",");
//				if (tokens.length != 53 && tokens.length != 54) continue;
//				StringBuilder sb = new StringBuilder();
//				sb.append(tokens[1].split(" ")[0]);
//				sb.append(COMMA_DELIMITER);
//				if (tokens.length == 53) {
//					sb.append(tokens[6]);
//					sb.append(COMMA_DELIMITER);
//					sb.append(tokens[8] + "-" + "Borough" + "-" + tokens[23]);
////					sb.append(COMMA_DELIMITER);
////					sb.append("City" + "-" + tokens[16]);
////					sb.append(COMMA_DELIMITER);
////					sb.append(tokens[22]);
////					sb.append(COMMA_DELIMITER);
////					sb.append("Borough" + "-" + tokens[23]);
//				} else if (tokens.length == 54) {
//					sb.append(tokens[6] + ":" + tokens[7]);
//					sb.append(COMMA_DELIMITER);
//					sb.append(tokens[9] + "-" + "Borough" + "-" + tokens[24]);
////					sb.append(COMMA_DELIMITER);
////					sb.append("City" + "-" + tokens[17]);
////					sb.append(COMMA_DELIMITER);
////					sb.append(tokens[23]);
////					sb.append(COMMA_DELIMITER);
////					sb.append("Borough" + "-" + tokens[24]);
//				}
//				pw.println(sb.toString());
//			}
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
				if (pw != null) pw.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private static String processTime(String time) {
		int second = Integer.parseInt(time);
		if (second >= 0 && second < 900) return "Duration: 0~15min";
		else if (second >= 900 && second < 1800) return "Duration: 15~30min";
		else if (second >= 1800 && second < 3600) return "Duration: 30~60min";
		else if (second >= 3600 && second < 7200) return "Duration: 1~2h";
		else if (second >= 7200 && second < 14400) return "Duration: 2~4h";
		else return "Duration: >=4h";
	}
	
	private static String processDate(String date) {
		if (date == null || date.length() == 0) return "";
		String[] tokens = date.split(" ");
		String[] time = tokens[1].split(":");
		return time[0] + ":00"; 
	}
	
	private static String processAge(String age) {
		if (age == null || age.length() == 0) return "N/A";
		int a = Integer.parseInt(age);
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int diff = year - a;
		return Integer.toString(diff / 10 * 10); 
	}
	
	private static String processGender(String gender) {
		if (gender.equals("1")) return "Male";
		else if (gender.equals("2")) return "Female";
		else return "N/A";
	}
}
