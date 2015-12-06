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
	public static void main(String[] args) throws URISyntaxException, IOException,
			ParseException, InterruptedException{
		String ACCESS_TOKEN = "";
		String vkIdUser = "";
		String vkNameUser = "";
		String vkNameEnemy = "";
		String vkIdEnemy = "";

		if(User.containLetters(vkIdUser))
			vkIdUser = User.getUserId(ACCESS_TOKEN, vkIdUser, "name");
		if(User.containLetters(vkIdEnemy))
			vkIdEnemy = User.getUserId(ACCESS_TOKEN, vkIdEnemy, "name");

		MessageGet messageGet = new MessageGet();
		messageGet.setIdUser(vkIdUser);
		messageGet.setToken(ACCESS_TOKEN);

		MessageSend messageSend = new MessageSend();
		messageSend.setIdUser(vkIdUser);
		messageSend.setToken(ACCESS_TOKEN);

		User userOffline = new User();
		userOffline.setToken(ACCESS_TOKEN);
		userOffline.setIdUser(vkIdUser);

		Thread t1 = new Thread(messageGet);
		Thread t2 = new Thread(messageSend);
		Thread t3 = new Thread(userOffline);
		Thread t4 = new Thread(userOffline);
		t4.start();
		t1.start();
		t2.start();
		t3.start();
//		MessageSend.sendMessageToId(ACCESS_TOKEN, vkIdUser, vkIdEnemy, "zalupa");
//		getLastMessage(ACCESS_TOKEN, vkIdUser);

//		getHistory(ACCESS_TOKEN, vkIdUser, vkIdEnemy);
	}

	public static void getHistory(String token, String idUser, String idEnemy) throws URISyntaxException, IOException,
			ParseException, InterruptedException{
		Scanner read = new Scanner(System.in);
		System.out.println("How much last messages do u want to see ? : ");
		String offset = read.next();
		int offsetInt = Integer.parseInt(offset.trim());

		PrintWriter writer = new PrintWriter(idUser + "to" + idEnemy + ".txt");
		for(int i = 0; i < offsetInt; i += 150){
			Thread.sleep(500);
			MessageGet.printMessagesHistory(token, idUser, idEnemy, Integer.toString(i), writer);
		}
		writer.close();
	}

	//read and send messages from one conversation
	//notifications
	//threads in simple console
	//graphical interface for simple console

	//REMAKE HISTORYmesage
	//read offset and enemy

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

	public static String getJSONfromVk(String urlForVk){    //this is usefull function
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
}

//https://oauth.vk.com/authorize?client_id=5144492&display=popup&scope=messages&response_type=token&v=5.40