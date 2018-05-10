package functionalities;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ViewportLayout;
import javax.swing.WindowConstants;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Algorithm {

	public ArrayList<JSONObject> json = new ArrayList<>();
	DefaultListModel<JSONObject> model = new DefaultListModel<>();
	JList<JSONObject> list;
	JFrame framePaho = new JFrame("Paho");
	JFrame frameMongo = new JFrame("Mongo");
	JFrame frameSybase = new JFrame("Sybase");
	JTextArea textAreaPaho;
	JTextArea textAreaMongo;
	JTextArea textAreaSybase;
	JScrollPane scrollPanePaho;
	JScrollPane scrollPaneMongo;
	JScrollPane scrollPaneSybase;
	
	public void init(){
		
		framePaho.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frameMongo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frameSybase.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		textAreaPaho = new JTextArea();
		textAreaMongo = new JTextArea();
		textAreaSybase = new JTextArea();
		scrollPanePaho = new JScrollPane(textAreaPaho);
		scrollPaneMongo = new JScrollPane(textAreaMongo);
		scrollPaneSybase = new JScrollPane(textAreaSybase);
		
		//frame.add(textAreaPaho);
		framePaho.add(scrollPanePaho);
		//frame.add(textAreaMongo);
		frameMongo.add(scrollPaneMongo);
		frameSybase.add(scrollPaneSybase);
		
		framePaho.setVisible(true);
		framePaho.setSize(500, 200);
		frameMongo.setLocation(new Point(0, 325));
		frameMongo.setVisible(true);
		frameMongo.setSize(700, 250);
		frameSybase.setLocation(new Point(550, 0));
		frameSybase.setVisible(true);
		frameSybase.setSize(500, 300);
		
	}
	
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
			model.addElement(jsonObject);
			list = new JList<JSONObject>(model);
			scrollPanePaho.setViewportView(list);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<JSONObject> getJSONArray(){
		return json;
	}
	
	public JScrollPane getScrollPaneMongo(){
		return scrollPaneMongo;
	}
	
	public JScrollPane getScrollPaneSybase(){
		return scrollPaneSybase;
	}
}
