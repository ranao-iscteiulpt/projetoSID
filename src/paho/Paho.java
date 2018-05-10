package paho;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import functionalities.Algorithm;

public class Paho implements MqttCallback {

	MqttClient client;
	Algorithm jsonAlgorithm = new Algorithm();

	public void main (Algorithm jsonAlgorithm) {
	    this.jsonAlgorithm = jsonAlgorithm;
	    doDemo();
	}

	public void doDemo() {
		
	    try {
	        client = new MqttClient("tcp://iot.eclipse.org:1883", "js-utility-19UPV");
	        client.connect();
	        System.out.println("Connection done!");
	        client.setCallback(this);
	        client.subscribe("foo");
	    } catch (MqttException e) {
	        e.printStackTrace();
	        System.out.println("Disconnect!");
	    }
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost!");
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//System.out.println(message);   
		jsonAlgorithm.insertDateTime(message);
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
	}
}