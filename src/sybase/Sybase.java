package sybase;

import java.sql.*;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.sybase.jdbc4.jdbc.SybDriver;

import functionalities.Algorithm;

public class Sybase {
	
	static final String JDBC_DRIVER = "com.sybase.jdbc4.jdbc.SybDriver";

	static final String DB_URL = "jdbc:sybase:Tds:localhost:2638?eng=Culturas";

	static final String USER = "Admin";
	static final String PASS = "admin";
	
	Algorithm jsonAlgorithm = new Algorithm();
	DefaultListModel<String> model = new DefaultListModel<>();
	JList<String> list;

	public void main(Algorithm jsonAlgorithm) {
		this.jsonAlgorithm = jsonAlgorithm;
		Connect();
	}
	
	public void Connect(){
		
		Connection conn = null;
	    Statement stmt = null;
	    
	    try {
	        Class.forName(JDBC_DRIVER);

	        //System.out.println("Connecting to database...");
	        conn = DriverManager.getConnection(DB_URL, USER, PASS);
	        //conn = DriverManager.getConnection(DB_URL);

	       //System.out.println("Creating statement...");
	        
	        stmt = conn.createStatement();
	        String sql;
	        ResultSet rs;
	        
	        //sql = "Insert into Investigador(email, nomeInvestigador) values ('investigador16@iscte-iul.pt', 'Investigador16')";
	        //stmt.execute(sql);
	        
	        sql = "SELECT * FROM Investigador";
	        rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            String email = rs.getString("email");
	            String nome = rs.getString("nomeInvestigador");
	            
	            model.addElement(email + nome);
	            //System.out.print("Email: " + email);
	            //System.out.print(", Nome: " + nome + "\n");
	        }
	        
	        list = new JList<>(model);
	        jsonAlgorithm.getScrollPaneSybase().setViewportView(list);
	        
	        rs.close();
	        stmt.close();
	        conn.close();
	    } catch (SQLException se) {
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        /*try {
	            if (stmt != null)
	                stmt.close();
	        } catch (SQLException se2) {
	        }// nothing we can do
	        try {
	            if (conn != null)
	                conn.close();
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }*/
	    }
	    
	    //System.out.println("Goodbye!");
	}	
}
