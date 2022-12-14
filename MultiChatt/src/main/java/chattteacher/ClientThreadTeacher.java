package chattteacher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClientThreadTeacher extends Thread{
	ClientMainTeacher main;
	BufferedWriter bw;
	BufferedReader br;
	Socket socket;
	boolean flag=true;
	
	public ClientThreadTeacher(Socket s, ClientMainTeacher m) {
		this.main = m;
		this.socket =s;
		try {
			OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
			bw = new BufferedWriter(osw);
			
			InputStreamReader isr = new InputStreamReader(s.getInputStream());
			br = new BufferedReader(isr);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		JSONParser parser = new JSONParser();
		flag=true;
		
		//서버에게 자신의 login 사실을 전달
		JSONObject loginObj = new JSONObject();
		loginObj.put("user", main.getTfUser().getText());
		loginObj.put("command", ServerMainTeacher.LOGIN);
		loginObj.put("message", "나야~ 나...");
		try {
			bw.write(loginObj.toJSONString());
			bw.write("\n");
			bw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		while(flag) {
			try {
				String msg = br.readLine();
				System.out.println(msg);
				if(msg == null) break;
				String u = "";
				
				JSONObject obj = (JSONObject)parser.parse(msg);
				
				main.getTextArea().append(obj.get("user")+" : ");
				main.getTextArea().append(obj.get("message")+"\n");
				
				main.getTextArea()
					.setCaretPosition(main.getTextArea().getText().length());
				
				Long  o = (Long)obj.get("command");
				
				switch(o.intValue()) {
				case ServerMainTeacher.SERVER_STOP:
					flag=false;
					break;
				case ServerMainTeacher.USERS:
					JSONArray array = (JSONArray)obj.get("data");
					if(array == null) break;
					main.userListModel.clear();
					for(Object ob : array) {
						main.userListModel.addElement((String)ob);
					}
					break;
				case ServerMainTeacher.LOGIN:
					u = (String)obj.get("user");
					main.userListModel.addElement(u);
					break;
				case ServerMainTeacher.LOGOUT:
					u = (String)obj.get("user");
					main.userListModel.removeElement(u);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			br.close();
			bw.close();
			socket.close();
			
			main.getBtnConnect().setEnabled(true);
			main.getBtnDisconnect().setEnabled(false);
			main.getBtnSend().setEnabled(false);
			main.getBtnWhisper().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("user", main.getTfUser().getText());
			obj.put("command", ServerMainTeacher.MESSAGE);
			obj.put("message", msg);
			bw.write(obj.toJSONString());
			bw.write("\n");
			bw.flush();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
