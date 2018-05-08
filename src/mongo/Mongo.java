package mongo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.bson.Document;
import org.bson.json.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;

import functionalities.Read;

public class Mongo extends Thread{
	
	private static MongoClient mongo = new MongoClient( "localhost" , 27017 ); ;
  	private MongoDatabase database = mongo.getDatabase("Culturas"); 
  	private MongoCollection<Document> collection = database.getCollection("HumidadeTemperatura");
	
	private Read read = new Read();
	
	public static void main( String args[] ) {  
		
		Mongo mongo = new Mongo();
		   
      	// Creating Credentials  
      	System.out.println("Connected to the database successfully");   
      	
      	mongo.start();
	} 
	
	@Override
	public void run() {
		
		while(true){
	      	
			if(read.getFile().exists()){
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONParser parser = new JSONParser();
				
				read.JSON();
	      	
				addCollection();
			}
				
	      	readCollection();	
		}
	}
	
	private void addCollection() {
		
		for (int i = 0; i < read.getJsonArray().size(); i++) {
			
			Double temperature = Double.parseDouble((String) read.getJsonArray().get(i).get("temperature"));
			Double humidity = Double.parseDouble((String) read.getJsonArray().get(i).get("humidity"));
			String date = (String) read.getJsonArray().get(i).get("date");
			String time = (String) read.getJsonArray().get(i).get("time");
			
			Document doc = new Document().append("temperature", temperature).append("humidity", humidity).append("date", date).append("time", time);
			
			collection.insertOne(doc);
		}
		
		System.out.println("Complete");

		read.getFile().delete();
		
		read.getJsonArray().clear();		
	}
	
	private void readCollection() {
		
      	MongoCursor<Document> cursor = (MongoCursor<Document>) collection.find().iterator();
		
      	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	
    	System.out.println("");
    	System.out.println("Coleção HumidadeTemperatura\n");
    	
    	while(cursor.hasNext()) {
    	    System.out.println(cursor.next().toJson());
    	}	
	}
}
