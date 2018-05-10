package sybase;

import java.sql.*;
import com.sybase.jdbc4.jdbc.SybDriver;

public class Sybase {
	
	static final String JDBC_DRIVER = "com.sybase.jdbc4.jdbc.SybDriver";

	static final String DB_URL = "jdbc:sybase:Tds:localhost:2638?eng=Culturas";

	static final String USER = "Admin";
	static final String PASS = "admin";

	public static void main(String[] args) {
		
		Sybase sybase = new Sybase();
		sybase.Connect();
	}
	
	public void Connect(){
		
		Connection conn = null;
	    Statement stmt = null;
	    
	    try {
	        Class.forName(JDBC_DRIVER);

	        System.out.println("Connecting to database...");
	        conn = DriverManager.getConnection(DB_URL, USER, PASS);
	        //conn = DriverManager.getConnection(DB_URL);

	        System.out.println("Creating statement...");
	        
	        stmt = conn.createStatement();
	        String sql;
	        ResultSet rs;
	        
	        sql = "Insert into Investigador(email, nomeInvestigador) values ('investigador16@iscte-iul.pt', 'Investigador16')";
	        stmt.execute(sql);
	        
	        sql = "SELECT * FROM Investigador";
	        rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            String email = rs.getString("email");
	            String nome = rs.getString("nomeInvestigador");

	            System.out.print("Email: " + email);
	            System.out.print(", Nome: " + nome + "\n");
	        }
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
	    
	    System.out.println("Goodbye!");
	}	
}
