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
public class MessageGet{
	public static String getHistoryMessages(String token, String vkId, String idEnemy, String offset){
		String urlForVk = "https://api.vk.com/method/messages.getHistory?&oid=" + vkId + "need_user=0" +
				"&out=0&user_id=" + idEnemy + "&offset=" + offset + "&count=150&rev=0&access_token=" + token;
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

	public static void parseJSONOfMessagesHistory(String token, String vkId, String idEnemy, String offset, PrintWriter writer)
			throws URISyntaxException, IOException, ParseException{
		JSONParser pars = new JSONParser();
		try {
			Object objectFinal = pars.parse(getHistoryMessages(token, vkId, idEnemy, offset));
			JSONObject objJsonFromVk = (JSONObject) objectFinal;
			JSONArray objectInJson = (JSONArray) objJsonFromVk.get("response");
			for(int i = 0; i < objectInJson.size() - 1; i++){   //must be size -5 | I don't know why -5 or -1 or -0 !?!
				JSONObject arrayInNumber = (JSONObject) objectInJson.get(i + 1);
				Object cache1 = arrayInNumber.get("out");
				if(outOrIn((long) cache1))
					writer.print("OUT : ");
				else
					writer.print("IN : ");
				writer.print((String) arrayInNumber.get("body"));
				writer.println(arrayInNumber.get("attachment"));
			}
			objectFinal = null;
			objJsonFromVk = null;
			objectInJson = null;

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean outOrIn(long value){
		if(value == 1)
			return true;
		else
			return false;
	}
}
