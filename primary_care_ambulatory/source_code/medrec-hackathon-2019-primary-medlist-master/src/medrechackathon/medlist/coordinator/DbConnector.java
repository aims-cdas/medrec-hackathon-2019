package medrechackathon.medlist.coordinator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hl7.fhir.dstu3.model.Patient;

public class DbConnector {

	private final String url = "jdbc:postgresql://localhost/mrh2019";
	private final String user = "postgres";
	private final String password = "";
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
	
	 public Connection connect() {
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url, user, password);
	         //   System.out.println("Connected to the PostgreSQL server successfully.");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	 
	        return conn;
	    }
	   public int checkPtSource(int id, String ehr) {
	        String SQL = "SELECT count(*) from mrh2019_pt where \"PtId\"=" + 
	        		id + 
	        			" AND ehr='" +
	        			ehr+ "'";
	        int count = 0;
	 
	        try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            rs.next();
	            count = rs.getInt(1);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	 
	        return count;
	    }
	 
	 
	   public int getPtFromCentralDb(Patient pt) {
	        String SQL = "SELECT count(*) from mrh2019_pt where first_name like '" + 
	        		(pt.getName().get(0)).getGivenAsSingleString() + 
	        			"' AND last_name='" +
	        			(pt.getName().get(0)).getFamily().toString() +
	        			"' AND dob='" + dateFormat.format(pt.getBirthDate()) +"'";
	        int count = 0;
	 
	        try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            rs.next();
	            count = rs.getInt(1);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	 
	        return count;
	    }
	   
	   public int getPtIdFromCentralDb(String firstName, String lastName) {
	        String SQL = "SELECT \"PtId\" from mrh2019_pt where first_name like '" + 
	        		firstName + 
	        			"' AND last_name='" +
	        			lastName +"'";
	    
	        int id=0;
	        try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            rs.next();
	            id = rs.getInt(1);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	 
	        return id;
	    }	   
	   
	   public int getPtFromCentralDb(String firstName, String lastName) {
	        String SQL = "SELECT count(*) from mrh2019_pt where first_name ilike '%" + 
	        		firstName + 
	        			"%' AND last_name='" +
	        			lastName +"'";
	        int count = 0;
	 
	        try (Connection conn = connect();
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            rs.next();
	            count = rs.getInt(1);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	 
	        return count;
	    }
	   
	   public void insertPtSource(int pt_id, String ehr, Patient pt, String base_url) {
	        String SQL = "INSERT INTO mrh2019_pt_sources(source_id,\"PtId\",ehr,id,base_url) "
	                + "VALUES(?,?,?,?,?)";
	        String nextVal = "SELECT nextval('pt_source_seq')";
	        String nextId = "";
	        long id = 0;
	 
	        try (Connection conn = connect();
		        	
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(nextVal)) {
	            rs.next();
	            nextId = rs.getString(1);
	            System.out.println("next id = " + nextId);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	

       try (Connection conn = connect();

	            PreparedStatement pstmt = conn.prepareStatement(SQL,
	                Statement.RETURN_GENERATED_KEYS)) {
	              Date date = new Date();
	            pstmt.setInt(1, Integer.parseInt(nextId));
	            pstmt.setInt(2, pt_id);
	            pstmt.setString(3, ehr);
	            pstmt.setString(4, pt.getIdBase());
	        	pstmt.setString(5, base_url);
	 
	            int affectedRows = pstmt.executeUpdate();
	            System.out.println(affectedRows);
	            // check the affected rows 
	        /*    if (affectedRows > 0) {
	                // get the ID back
	                try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                    if (rs.next()) {
	                        id = rs.getLong(1);
	                    }
	                } catch (SQLException ex) {
	                    System.out.println(ex.getMessage());
	                }
	            } */
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	       // return id;
	    }
	   
	   
	 
	   public int insertPt(Patient pt) {
	        String SQL = "INSERT INTO mrh2019_pt(\"PtId\", first_name,last_name,dob,create_date) "
	                + "VALUES(?,?,?,?,?)";
	        String nextVal = "SELECT nextval('pt_seq')";
	        String nextId = "";
	        int id = 0;
	 
	        try (Connection conn = connect();
		        	
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(nextVal)) {
	            rs.next();
	            nextId = rs.getString(1);
	            System.out.println("next id = " + nextId);
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	

        try (Connection conn = connect();

	            PreparedStatement pstmt = conn.prepareStatement(SQL,
	                Statement.RETURN_GENERATED_KEYS)) {
	              Date date = new Date();
	            pstmt.setInt(1, Integer.parseInt(nextId));
	            pstmt.setString(2, (pt.getName().get(0)).getGivenAsSingleString());
	            pstmt.setString(3, (pt.getName().get(0)).getFamily().toString());
	            pstmt.setString(4, dateFormat.format(pt.getBirthDate()));
	        	pstmt.setString(5, dateFormat.format(date));
	 
	            int affectedRows = pstmt.executeUpdate();
	            System.out.println(affectedRows);
	            // check the affected rows 
	            if (affectedRows > 0) {
	                // get the ID back
	                try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                    if (rs.next()) {
	                        id = rs.getInt(1);
	                    }
	                } catch (SQLException ex) {
	                    System.out.println(ex.getMessage());
	                }
	            } 
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
        System.out.println("ID entered is: " +id);
	        return id;
	    }
	 public static void main (String[] args) {
	//	 DbConnector dbc= new DbConnector();
		// dbc.connect();
		//int pt_count= dbc.getPtFromCentralDb("Millie Janine", "Bryant");
		//System.out.println("I found " + pt_count + " instance(s) of Millie Janine Bryant");
		 
	 
	 }
}
