package functionalities;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Algorithm {

	public ArrayList<JSONObject> json = new ArrayList<>();
	
	public void insertDateTime(MqttMessage message){
	
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		
		String messageString = "{" + message.toString() + ",\"date\":\"" + dateFormat.format(date) + "\",\"time\":\"" + timeFormat.format(time) + "\"}";
		
		insertToJSONArray(messageString);
	}

	public void insertToJSONArray(String messageString) {
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(messageString);
			json.add(jsonObject);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<JSONObject> getJSONArray(){
		return json;
	}
}
