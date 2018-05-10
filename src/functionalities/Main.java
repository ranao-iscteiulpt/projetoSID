package functionalities;

import mongo.Mongo;
import paho.Paho;
import sybase.Sybase;

public class Main {

	public static void main(String[] args) {
		
		Algorithm jsonAlgorithm = new Algorithm();
		jsonAlgorithm.init();
		Paho paho = new Paho();
		Mongo mongo = new Mongo();
		Sybase sybase = new Sybase();
		paho.main(jsonAlgorithm);
		mongo.main(jsonAlgorithm);
		sybase.main(jsonAlgorithm);
     	mongo.start();
	}

}
