package functionalities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Write {
	
	private File file = new File("messagemSensor.json");
	
	public void dateTime(MqttMessage message){
	
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		
		String messageString = "{" + message.toString() + ",\"date\":\"" + dateFormat.format(date) + "\",\"time\":\"" + timeFormat.format(time) + "\"}";
		
		writeToJSON(messageString);
	}

	private void writeToJSON(String messageString) {
		
		FileWriter fileWriter = null;
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
						
		try {
			
			fileWriter = new FileWriter(file, true);
			
			fileWriter.append(messageString + "\n");
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			String s = (String) jsonObject.get("temperature");
			System.out.println(s);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
