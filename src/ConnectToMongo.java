import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

public class ConnectToMongo {
	
	public static void main( String args[] ) {  
		
      // Creating a Mongo client 
      MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
   
      // Creating Credentials  
      System.out.println("Connected to the database successfully");  
      
      // Accessing the database 
      MongoDatabase database = mongo.getDatabase("Culturas");  
      System.out.println(database.getCollection("HumidadeTemperatura").getDocumentClass());
   } 
}
