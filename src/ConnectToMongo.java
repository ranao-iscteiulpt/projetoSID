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

public class ConnectToMongo extends Thread{
	
	private static MongoClient mongo = new MongoClient( "localhost" , 27017 ); ;
  	private MongoDatabase database = mongo.getDatabase("Culturas"); 
  	private MongoCollection collection = database.getCollection("HumidadeTemperatura");
	
	private File f = new File("messagemSensor.json");
  	
    private ArrayList<JSONObject> json = new ArrayList<JSONObject>();
    private JSONObject jsonObject;
    
	
	public static void main( String args[] ) {  
		
		ConnectToMongo connectToMongo = new ConnectToMongo();
		   
      	// Creating Credentials  
      	System.out.println("Connected to the database successfully");   
      	
      	connectToMongo.start();
	} 
	
	@Override
	public void run() {
		
		while(true){
	      	
			if(f.exists()){
			
				readJSON();
	      	
	      		addCollection();
			}
			
	      	readCollection();	
		}
	}

	private void readJSON() {
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONParser parser = new JSONParser();
		
		try {
			
	        // FileReader reads text files in the default encoding.
	        FileReader fileReader = new FileReader("messagemSensor.json");

	        // Wrap FileReader in BufferedReader.
	        BufferedReader bufferedReader = new BufferedReader(fileReader);

	        String line;
	       
	        while((line = bufferedReader.readLine()) != null) {
	        	jsonObject = (JSONObject) new JSONParser().parse(line);
	            json.add(jsonObject);
	        }
	        
	        // Close files.
	        bufferedReader.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addCollection() {
		
		for (int i = 0; i < json.size(); i++) {
			
			Double temperature = Double.parseDouble((String) json.get(i).get("temperature"));
			Double humidity = Double.parseDouble((String) json.get(i).get("humidity"));
			String date = (String) json.get(i).get("date");
			String time = (String) json.get(i).get("time");
			
			Document doc = new Document().append("temperature", temperature).append("humidity", humidity).append("date", date).append("time", time);
			
			collection.insertOne(doc);
		}
		
		System.out.println("Complete");

		f.delete();
		
		json.clear();		
	}
	
	private void readCollection() {
		
      	MongoCursor<Document> cursor = (MongoCursor) collection.find().iterator();
		
      	try {
			Thread.sleep(2000);
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
