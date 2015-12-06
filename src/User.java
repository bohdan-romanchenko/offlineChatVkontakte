import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by nadman on 12.11.15.
 */
public class User implements Runnable{

	private String token;
	private String idUser;

	public void run(){
		System.out.println("Thread 1");
		try{
			System.out.println(setOffline(token, idUser));
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

	public static String getJSONAboutUser(String token, String idEnemy){
		String urlForVk = "https://api.vk.com/method/users.search?&q=" + idEnemy + "&count=1&access_token=" + token;
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000);
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

	public static String getUserId(String token, String nameEnemy, String fullName){
		JSONParser pars = new JSONParser();
		String jsonFromVk = getJSONAboutUser(token, nameEnemy);
		String id = "something wrong with ID";
		try {
			Object objectFinal = pars.parse(jsonFromVk);
			JSONObject objJsonFromVk = (JSONObject) objectFinal;
			JSONArray objectInJson = (JSONArray) objJsonFromVk.get("response");
			JSONObject arrayInNumber = (JSONObject) objectInJson.get(1);
			Object cache1 = arrayInNumber.get("uid");
			long cache2 = (long) cache1;                //maybe without long variable. From Obj to String
			id = Long.toString(cache2);
//			fullName = arrayInNumber.get("first_name") + (String) arrayInNumber.get("last_name");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static String setOffline(String token, String idUser){
		String urlForVk = "https://api.vk.com/method/account.setOffline?&q=" + idUser + "&access_token=" + token;
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000);
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

	public static boolean containLetters(String str){
		if(str.replaceAll("[a-zA-Z]+", "CHANGED").contains("CHANGED"))
			return true;
		return false;
	}
}
