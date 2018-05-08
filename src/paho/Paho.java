package paho;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import functionalities.Write;

public class Paho implements MqttCallback {

	MqttClient client;
	Write write = new Write();

	public static void main(String[] args) {
	    new Paho().doDemo();
	}

	public void doDemo() {
	    try {
	        client = new MqttClient("tcp://iot.eclipse.org:1883", "js-utility-4RQe1");
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
		System.out.println(message);   
		write.dateTime(message);
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
	}
}