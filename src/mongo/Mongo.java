package mongo;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.bson.Document;
import org.json.simple.JSONObject;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import functionalities.Algorithm;

public class Mongo extends Thread{
	
	private static MongoClient mongo = new MongoClient( "localhost" , 27017 );
  	private MongoDatabase database = mongo.getDatabase("Culturas"); 
  	private MongoCollection<Document> collection = database.getCollection("HumidadeTemperatura");
	DefaultListModel<String> model = new DefaultListModel<>();
	JList<String> list;

	private Algorithm jsonAlgorithm = new Algorithm();
	
	public void main(Algorithm jsonAlgorithm) {  
		this.jsonAlgorithm = jsonAlgorithm; 
	
      	// Creating Credentials  
      	System.out.println("Connected to the database successfully"); 
	} 
	
	@Override
	public void run() {
		
		while(true){
	      					
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			addCollection();
				
	      	readCollection();	
		}
	}
	
	private void addCollection() {
		
		for (int i = 0; i < jsonAlgorithm.getJSONArray().size(); i++) {
			
			Double temperature = Double.parseDouble((String) jsonAlgorithm.getJSONArray().get(i).get("temperature"));
			Double humidity = Double.parseDouble((String) jsonAlgorithm.getJSONArray().get(i).get("humidity"));
			String date = (String) jsonAlgorithm.getJSONArray().get(i).get("date");
			String time = (String) jsonAlgorithm.getJSONArray().get(i).get("time");
			
			Document doc = new Document().append("temperature", temperature).append("humidity", humidity).append("date", date).append("time", time);
			
			collection.insertOne(doc);
		}
		
		jsonAlgorithm.getJSONArray().clear();
	}
	
	private void readCollection() {
		
		model.clear();
		
      	MongoCursor<Document> cursor = (MongoCursor<Document>) collection.find().iterator();
		
      	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	    	
    	model.addElement("Coleção HumidadeTemperatura\n");
    	model.addElement("");
    	
    	while(cursor.hasNext()) {
    	    //System.out.println(cursor.next().toJson());
    	    model.addElement(cursor.next().toJson().toString());
    	}
    	
    	list = new JList<>(model);
    	jsonAlgorithm.getScrollPane().setViewportView(list);
	}
}
