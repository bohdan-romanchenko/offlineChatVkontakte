import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by nadman on 11.11.15.
 */
public class offlineChatVkontakte{
	public static void main(String[] args){
		String ACCESS_TOKEN = getACCESS_TOKEN();
		String vkId = "17967062";
		String appId = "5144492";
		String urlForVk = "https://api.vk.com/method/audio.search?&" +
				"oid=" + vkId + "need_user=0&" +
				"q=" + "????" + "&auto_complete=0&" +
				"lyrics=0&performer_only=1&" +
				"sort=2&search_own=0&offset=1&count=300&" +
				"access_token=" + ACCESS_TOKEN;
	}

	public static String getACCESS_TOKEN(){
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
		return "8d844d43325a2c4ec9c7200963833ff3d8cbb07af8dac598e822c47b7552a86e514e2315fa035ab5ee6dc";
	}

	public static String getJSONfromVk(String urlForVk){
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