package sybase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.client.MongoCursor;

import functionalities.Frame;
import mongo.Mongo;


public class Sybase extends Thread{
	
	static final String JDBC_DRIVER = "com.sybase.jdbc4.jdbc.SybDriver"; // Sybase driver

	static String DB_URL = null; 

	// Credenciais (username e password) de acesso ao Sybase
	
	static final String USER = "Sensor";
	static final String PASS = "sensor";
	//---
	
	private Connection conn = null;
    private Statement stmt = null;
    
	private Frame frame;

	// Modelo e lista para inserir posteriormente no scrollPane
	
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> list;
	
	private boolean connect = false;
	
	private Mongo mongo;

	public Sybase(Frame frame, Mongo mongo){
		this.frame = frame;
		this.mongo = mongo;
	}
	
	// Metodo que vai criar URL para a conexão ao Sybase com o respectivo IP do servidor e a base de dados Sybase
	
	public void main() {
		
		frame.getIPButtonSybase().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		
				DB_URL = "jdbc:sybase:Tds:" + frame.getIPText().getText() + ":2638?eng=Culturas";
				start();
			}
		});
		
		frame.getFrequencyButtonSybase().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				interrupt();
				
			}
		});
	}
	
	@Override
	public void run() {
		
		while(true){
			
			Connect();
			
    		try {
    			Thread.sleep(frame.getTimerSybase());
			} catch (InterruptedException a) {
				// TODO Auto-generated catch block
				a.printStackTrace();
			}
			
		}
	}
	
	// Metodo para efetuar a conexão ao Sybase
	
	public void Connect(){
		
	    try {
	    	if(!connect){
				
		        Class.forName(JDBC_DRIVER);
	
		        System.out.println("Connecting to database...");
		        conn = DriverManager.getConnection(DB_URL, USER, PASS);

		        connect = true;
		        
		        stmt = conn.createStatement();
		        
				select();
	    	}
	    	
	    	else{
	    		
	    		stmt = conn.createStatement();
	    		
	    		addToSybase();
	        
	        	select();
	    	}
	        stmt.close();
//	        conn.close();
	    } catch (SQLException se) {
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {

	    }
	}	
	
	/* Metodo para inserir dados no sybase na tabela HumidadeTemperatura por cada dos inserido 
	com sucesso e inserido na coleção BackUp do MongoDB */
	
	public void addToSybase(){
		
        String sql;
        
        if(mongo.getConnect() == true){    
        	
			MongoCursor<Document> cursor = (MongoCursor<Document>) mongo.getHT().find().iterator();
			
	    	while(cursor.hasNext()) {
	    	    
	    		JSONParser parser = new JSONParser();
	    		JSONObject json = null;
				try {
					json = (JSONObject) parser.parse(cursor.next().toJson().toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
	    		Timestamp date = Timestamp.valueOf(json.get("date").toString());
	    		Time time = Time.valueOf(json.get("time").toString());
	    		Double temperatura = (Double) json.get("temperature");
	    		Double humidade = (Double) json.get("humidity");
	    		
	    		mongo.addBackUp(temperatura, humidade, date, time);
	    		mongo.getModelHT().clear();
	    			    		
		        sql = "call Admin.Insert_HumidadeTemperatura(" + "'" + date + "'" + ", " + "'" + time + "'" + ", " +  temperatura + ", " + humidade +")";
		       // System.out.println(sql);
		        try {
					stmt.execute(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       	}
	        
	    	mongo.getHT().deleteMany(new Document());
	    	
	    	mongo.getModelHT().clear();
        }
	}
	
	// Metodo que efetua o select da tabela HumidadeTemperatura para ser possivel visualizar na frame
	
	public void select(){
		
        String sql;
        ResultSet rs = null;
    	        
        sql = "call Admin.Select_HumidadeTemperatura";
        try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        model.clear();
        
        try {
			while (rs.next()) {
			    String data = rs.getString("dataMedicao");
			    String hora = rs.getString("horaMedicao");
			    Double temperatura = rs.getDouble("valorMedicaoTemperatura");
			    Double humidade = rs.getDouble("valorMedicaoHumidade");
			    int id = rs.getInt("iDMedicao");
			    
			    model.addElement(data + " - " + hora + " - " + temperatura + " - " + humidade + " - " + id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        list = new JList<>(model);
        frame.getScrollPaneSybase().setViewportView(list);
        
        try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
