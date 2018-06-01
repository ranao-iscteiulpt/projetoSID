package functionalities;

import mongo.Mongo;
import paho.Paho;
import sybase.Sybase;

public class Main {

	public static void main(String[] args) {
		
		Frame frame = new Frame();
		frame.init();
		
		Paho paho = new Paho(frame);
		Mongo mongo = new Mongo(frame);
		Sybase sybase = new Sybase(frame, mongo);
		
		Algorithm algorithm = new Algorithm(frame);
		
		paho.main(algorithm);
		mongo.main(algorithm);
		sybase.main();
	}

}
