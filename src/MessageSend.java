import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nadman on 12.11.15.
 */
public class MessageSend implements Runnable{
	private String token;
	private String idUser;
	private String idEnemy;
	private String message;

	public void run(){
		System.out.println("Thread 2");
		while(true){
			System.out.println("Input recipient's ID or path : ");
			Scanner scanner = new Scanner(System.in);
			idEnemy = scanner.nextLine();
			if(User.containLetters(idEnemy))
				idEnemy = User.getUserId(token, idEnemy, "name");
			System.out.println("Input your message to " + idEnemy + " : ");
			message = scanner.nextLine();
			if(!(message.trim()).equalsIgnoreCase("cancel"))
				sendMessageToId(token, idUser, idEnemy, message);
			System.out.println(message);
		}
	}

	public void setToken(String token1){
		token = token1;
	}

	public void setIdUser(String idUser1){
		idUser = idUser1;
	}

	public static void sendMessageToId(String token, String idUser, String idEnemy, String message){
		String urlForVk = "https://api.vk.com/method/messages.send?&oid=" + idUser +
				"&user_id=" + idEnemy + "&message=" + message + "&access_token=" + token;
		try{
			URL url = new URL(urlForVk);
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
			con.setConnectTimeout(600);
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
			} else
				System.out.println("Error " + resp);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
