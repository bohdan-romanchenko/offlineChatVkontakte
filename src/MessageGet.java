import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by nadman on 12.11.15.
 */
public class MessageGet implements Runnable{
	private String token;
	private String idUser;

	public void run(){
		System.out.println("Thread 1");
		try{
			printLastMessageToMain(token, idUser);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setToken(String token1){
		token = token1;
	}

	public void setIdUser(String idUser1){
		idUser = idUser1;
	}

	public static void printMessagesHistory(String token, String vkId, String idEnemy,
	                                        String offset, PrintWriter writer)
			throws URISyntaxException, IOException, ParseException{
		JSONParser pars = new JSONParser();
		try {
			Object objectFinal = pars.parse(getHistoryMessages(token, vkId, idEnemy, offset));
			JSONObject objJsonFromVk = (JSONObject) objectFinal;
			JSONArray objectInJson = (JSONArray) objJsonFromVk.get("response");
			for(int i = 0; i < objectInJson.size() - 1; i++){
				JSONObject arrayInNumber = (JSONObject) objectInJson.get(i + 1);
				Object cache1 = arrayInNumber.get("out");
				if(outOrIn((long) cache1))
					writer.print("OUT : ");
				else
					writer.print("IN : ");
				writer.print((String) arrayInNumber.get("body"));
				writer.println(arrayInNumber.get("attachment"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getHistoryMessages(String token, String idUser, String idEnemy, String offset){
		String urlForVk = "https://api.vk.com/method/messages.getHistory?&oid=" + idUser + "need_user=0" +
				"&out=0&user_id=" + idEnemy + "&offset=" + offset + "&count=150&rev=0&access_token=" + token;
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(2000);
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

	public static void printLastMessageToMain(String token, String idUser) throws URISyntaxException, IOException,
			ParseException, InterruptedException{
		long prevTime = 0;
		while(true){
			if(getLastTime(token, idUser) > prevTime){
				prevTime = getLastTime(token, idUser);
				MessageGet.printLastMessage(token, idUser);
			}
			Thread.sleep(800);
		}
	}

	public static String getLastMessage(String token, String idUser){
		String urlForVk = "https://api.vk.com/method/messages.get?&oid=" + idUser +
				"&out=0&&filters=4&offset=0&out=0&count=1&rev=0&access_token=" + token;
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(2000);
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

	public static void printLastMessage(String token, String idUser) throws URISyntaxException, IOException,
			ParseException{
		JSONParser pars = new JSONParser();
		try {
			Object objectFinal = pars.parse(getLastMessage(token, idUser));
			JSONObject objJsonFromVk = (JSONObject) objectFinal;
			JSONArray objectInJson = (JSONArray) objJsonFromVk.get("response");
			JSONObject arrayInNumber = (JSONObject) objectInJson.get(1);
			System.out.print("FROM " + arrayInNumber.get("uid") + " : ");
			if(arrayInNumber.get("attachment") != null){
				System.out.print((String) arrayInNumber.get("body"));
				System.out.println(arrayInNumber.get("attachment"));
			}
			else
				System.out.println((String) arrayInNumber.get("body"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static long getLastTime(String token, String idUser)throws URISyntaxException, IOException,
			ParseException{
		JSONParser pars = new JSONParser();
		long time = 0;
		try {
			Object objectFinal = pars.parse(getLastMessage(token, idUser));
			JSONObject objJsonFromVk = (JSONObject) objectFinal;
			JSONArray objectInJson = (JSONArray) objJsonFromVk.get("response");
			JSONObject arrayInNumber = (JSONObject) objectInJson.get(1);
			Object cache1 = arrayInNumber.get("date");
			String cache2 = cache1.toString();
			time = Long.parseLong(cache2);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static boolean outOrIn(long value){
		if(value == 1)
			return true;
		else
			return false;
	}
}
