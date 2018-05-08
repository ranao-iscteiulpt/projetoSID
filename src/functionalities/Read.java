package functionalities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Read {

	private File f = new File("messagemSensor.json");
	
    private ArrayList<JSONObject> json = new ArrayList<JSONObject>();
    private JSONObject jsonObject;
	
	public void JSON(){
		
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
	
	public File getFile(){
		return f;
	}
	
	public ArrayList<JSONObject> getJsonArray(){
		return json;
	}
	
}
