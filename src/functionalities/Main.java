package functionalities;

import mongo.Mongo;
import paho.Paho;

public class Main {

	public static void main(String[] args) {
		
		Algorithm jsonAlgorithm = new Algorithm();
		jsonAlgorithm.init();
		Paho paho = new Paho();
		Mongo mongo = new Mongo();
		paho.main(jsonAlgorithm);
		mongo.main(jsonAlgorithm);
     	mongo.start();
	}

}
