import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Created by nadman on 11.11.15.
 */
public class offlineChatVkontakte{
	public static void main(String[] args) throws URISyntaxException, IOException, ParseException, InterruptedException{
//		String ACCESS_TOKEN = getACCESS_TOKEN();
		String ACCESS_TOKEN = "";
		String vkIdUser = "";
		String vkNameUser = "Bohdan";
		String vkNameEnemy = "";
		String vkIdEnemy = "b.nadman";

		if(containLetters(vkIdUser))
			vkIdUser = User.getUserId(ACCESS_TOKEN, vkIdUser, "name");
		if(containLetters(vkIdEnemy))
			vkIdEnemy = User.getUserId(ACCESS_TOKEN, vkIdEnemy, "name");

		Scanner read = new Scanner(System.in);
		System.out.println("How much last messages do u want to still ? : ");
		String offset = read.next();
		int offsetInt = Integer.parseInt(offset.trim());

		PrintWriter writer = new PrintWriter(vkIdUser + "to" + vkIdEnemy + ".txt");
		for(int i = 0; i < offsetInt; i += 150){
			Thread.sleep(334);
			MessageGet.parseJSONOfMessagesHistory(ACCESS_TOKEN, vkIdEnemy, vkIdEnemy, Integer.toString(i), writer);
		}
		writer.close();

//		MessageGet.parseJSONOfMessagesHistory(ACCESS_TOKEN, vkIdEnemy, vkIdEnemy, "150", writer);
//		MessageGet.parseJSONOfMessagesHistory(ACCESS_TOKEN, vkIdEnemy, vkIdEnemy, "300", writer);
	}

	public static boolean containLetters(String str){
		if(str.replaceAll("[a-zA-Z]+", "CHANGED").contains("CHANGED"))
			return true;
		return false;
	}

	public static String getACCESS_TOKEN(){ //in future here i will easy take access token
		String urlForVk = "https://oauth.vk.com/authorize" +
				"?client_id=5144492" +
				"&display=popup" +
				"&scope=messages" +
				"&response_type=token" +
				"&v=5.40";
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(334);
			con.connect();
			int resp = con.getResponseCode();
			if(resp == 200 || resp == 6){
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine()) != null){
					sb.append(line);
					sb.append("\n");
				}
				br.close();
				return sb.toString();
			} else
				System.out.println("Error " + resp);
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getJSONfromVk(String urlForVk){    //this is usefull function
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(334);
			con.connect();
			int resp = con.getResponseCode();
			if(resp == 200 || resp == 6){
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine()) != null){
					sb.append(line);
					sb.append("\n");
				}
				br.close();
				return sb.toString();
			} else
				System.out.println("Error " + resp);
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}