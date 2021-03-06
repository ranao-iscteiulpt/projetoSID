package functionalities;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mongo.Mongo;

public class Algorithm {

	private ArrayList<JSONObject> json = new ArrayList<>(); // Array com as mensagens recebidas e tratadas
	
	private DefaultListModel<String> model = new DefaultListModel<>(); // Modelo que vai ser adicionado a lista para inserir na textArea
	private JList<String> list; // Lista que tem o modelo e que vai ser inserido na textArea
	
	private Frame frame;
	
	private JSONObject messageToConfirm = new JSONObject(); // Mensagem em JSON que vai ser confirmada para ser ou n�o enviada para o MongoDB
	private String messageString = null; // Messagem recebido em formato String
	
	private boolean send = false; // Booleano que indica se a mensagem e para ser enviada ou n�o para o MongoDB
	
	public Algorithm (Frame frame){
		this.frame = frame;
	}
	
	// Metodo para inserir data e hora para testes mais rapidos e para a parte manual
	
	public void insertDateTime(MqttMessage message){
	
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		
		messageString = "{" + message.toString() + ",\"date\":\"" + dateFormat.format(date) + "\",\"time\":\"" + timeFormat.format(time) + "\"}";
	}
	
	/* Metodo que vai confirmar se a mensagem � uma mensagem para ser enviada para o MongoDB ou nao, 
	alterando o valor do booleano send para false caso n�o e true caso a mensagem seja para ser enviada */
	
	public void confirm(MqttMessage message) {
		
		JSONParser parser = new JSONParser();
		String erradas = null;
		
		try {
			if(message.toString().contains("{") && message.toString().contains("}")){
				messageToConfirm = (JSONObject) parser.parse(message.toString());
			}
			else{
				erradas = message.toString();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(message.toString().equals("teste")){
			send = false;
		} 
		else if(message.toString().equals("")){
			send = false;
		} 
		else if(!messageToConfirm.containsKey("temperature")){
			send = false;
			erradas = message.toString();
		}
		else if(messageToConfirm.get("temperature").equals("") || Pattern.matches("[a-zA-Z]+", messageToConfirm.get("temperature").toString())){
			send = false;
			erradas = message.toString();
		}
		else if(!messageToConfirm.containsKey("humidity")){
			send = false;
			erradas = message.toString();
		}
		else if(messageToConfirm.get("humidity").equals("") || Pattern.matches("[a-zA-Z]+", messageToConfirm.get("humidity").toString())){
			send = false;
			erradas = message.toString();
		}
		else if(!messageToConfirm.containsKey("date")){
			send = false;
			erradas = message.toString();
		}
		else if(messageToConfirm.get("date").equals("") || Pattern.matches("[a-zA-Z]+", messageToConfirm.get("date").toString())){
			send = false;
			erradas = message.toString();
		}
		else if(!messageToConfirm.containsKey("time")){
			send = false;
			erradas = message.toString();
		}
		else if(Pattern.matches("[a-zA-Z]+", messageToConfirm.get("time").toString())){
			send = false;
			erradas = message.toString();
		}
		else{
			send = true;
		}
		
		if(send == true){
			addTextArea(messageToConfirm.toString());
		}
		else{
			addTextArea(erradas);
		}
	}
	
	/* Metodo que vai utilizar a mensagem j� confirmada e vai fazer as convers�es necess�rias, i.e, String para double no caso da
	temperatura e humidade, timestamp para a data e time para a hora */
	
	public void conversion(){
		
		Double temperature = Double.parseDouble((String) messageToConfirm.get("temperature"));
		Double humidity = Double.parseDouble((String) messageToConfirm.get("humidity"));
		String date = (String) messageToConfirm.get("date");
		String time = (String) messageToConfirm.get("time");
		
		Time timeTemp = Time.valueOf(time);
		@SuppressWarnings("deprecation")
		Time timeSQL = Time.valueOf((timeTemp.getHours()) + ":" + timeTemp.getMinutes() + ":" + timeTemp.getSeconds()); 
		
		String[] tokensDate = date.split("/");
		Timestamp dateSQL = Timestamp.valueOf(tokensDate[2] + "-" + tokensDate[1] + "-" + tokensDate[0] + " " + time);
		
		messageString = "{" + "\"temperature\":" + temperature + ",\"humidity\":"+ humidity + ",\"date\":\"" + dateSQL + "\",\"time\":\"" + timeSQL + "\"}";
	}
	
	// Metodo que vai inserir as mensagens comfirmadas e tratadas num array para posteriomente serem enviadas para o MongoDB
	
	public void insertArray(String messageString){
		
		JSONParser parser = new JSONParser();
		JSONObject messageJson = new JSONObject();
		try {
			messageJson = (JSONObject) parser.parse(messageString);
			json.add(messageJson);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Metodo que vai inserir todas as mensagens na textArea para serem visualizadas na frame
	
	public void addTextArea(String messageString){
		model.addElement(messageString);
		list = new JList<String>(model);
		frame.getScrollPanePaho().setViewportView(list);
	}
	
	public ArrayList<JSONObject> getJSONArray(){
		return json;
	}
	
	public String getMessageString(){
		return messageString;
	}
	
	public boolean getSend(){
		return send;
	}
}