package mongo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import functionalities.Algorithm;
import functionalities.Frame;

public class Mongo extends Thread{
	
	private static MongoClient mongo; // Cliente MongoDB
	
  	private MongoDatabase database; // Base de dados MongoDB
  	
  	// Cole��es da base de dados MongoDB 
  	
  	private MongoCollection<Document> HumidadeTemperatura;
  	private MongoCollection<Document> BackUp;
  	
  	// Modelos e lista para inserir posteriormente no scrollPane
  	
	private DefaultListModel<String> modelHT = new DefaultListModel<>();
	private DefaultListModel<String> modelB = new DefaultListModel<>();
	private JList<String> list;
	//---
	
	private Frame frame;
	private Algorithm algorithm = new Algorithm(frame);
	private boolean connect = false;

	public Mongo(Frame frame){
		this.frame = frame;
	}
	
	// Metodo que vai iniciar o cliente MongoDB e acessar a base de dados e cole��es
	
	public void main(Algorithm algorithm) {  
		
		this.algorithm = algorithm; 
		
		frame.getHostButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ServerAddress serverAddress = new ServerAddress(frame.getHostText().getText(), 27017);  
				MongoCredential credential = MongoCredential.createCredential("admin", "MedicoesSensor", "admin".toCharArray());
				mongo = new MongoClient(serverAddress, Arrays.asList(credential));
				database = mongo.getDatabase("MedicoesSensor");
				HumidadeTemperatura = database.getCollection("HumidadeTemperatura");
				BackUp = database.getCollection("BackUp");
				connect = true;
				start();
			}
		});
		
		frame.getFrequencyButtonMongo().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				interrupt();
			}
		}); 
	} 
	
	// Metodo que vai chamar o mtodo para inserir dados na cole��o principal HumidadeTemperatura consoante o timer escolhido
	
	@Override
	public void run() {
		
		readCollection();
		readBackUp();
		
		
		while(true){
	      			
			try {
				Thread.sleep(frame.getTimerMongo());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			if(algorithm.getJSONArray().size() > 0){
				addCollection();
				
		      	readCollection();
		      	readBackUp();
			}
		}
	}
	
	// Metodo para inserir dados na cole��o HumidadeTemperatura
	
	private void addCollection() {
		
		Document doc = new Document();
		
		if(!algorithm.getJSONArray().isEmpty()){
			
			for (int i = 0; i < algorithm.getJSONArray().size(); i++) {
								
				doc = Document.parse(algorithm.getJSONArray().get(i).toString());
				
				HumidadeTemperatura.insertOne(doc);
			}
			
			algorithm.getJSONArray().clear();
			
			readCollection();
		}
	}
	
	// Metodo para inserir dados na cole��o BackUp (dados que foram enviados com sucesso para o sybase)
	
	public void addBackUp(Double temperatura, Double humidade, Timestamp date, Time time) {
			
		Document doc = new Document().append("temperature", temperatura).append("humidity", humidade).append("date", date).append("time", time);
		
		BackUp.insertOne(doc);
		
		readBackUp();
	}
	
	// Metodo para ler cole��o BackUp e inserir dados no ScrollPane para serem visualizados na frame
	
	private void readBackUp() {
		
		modelB.clear();
		
      	MongoCursor<Document> cursor = (MongoCursor<Document>) BackUp.find().iterator();

    	while(cursor.hasNext()) {
    	    modelB.addElement(cursor.next().toJson().toString());
       	}

    	list = new JList<>(modelB);
    	frame.getScrollPaneMongoB().setViewportView(list);
	}
	
	// Metodo para ler cole��o HumidadeTemperatura e inserir dados no ScrollPane para serem visualizados na frame
	
	private void readCollection() {
	
		modelHT.clear();
		
      	MongoCursor<Document> cursor = (MongoCursor<Document>) HumidadeTemperatura.find().iterator();

    	while(cursor.hasNext()) {
    	    modelHT.addElement(cursor.next().toJson().toString());
       	}

    	list = new JList<>(modelHT);
    	frame.getScrollPaneMongoHT().setViewportView(list);
	}
	
	public MongoCollection<Document> getHT(){
		return HumidadeTemperatura;
	}
	
	public DefaultListModel<String> getModelHT(){
		return modelHT;
	}
	
	public boolean getConnect(){
		return connect;
	}
}
