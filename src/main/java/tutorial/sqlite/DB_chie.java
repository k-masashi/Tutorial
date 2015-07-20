package tutorial.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DB_chie {
	private Connection con = null;
	private Statement smt = null;
	private ResultSet rs = null;

	private PreparedStatement prep_QUESTION;
	private PreparedStatement prep_TOPIC;
	private PreparedStatement prep_WORD;
	private PreparedStatement prep_DOCMENT;
	private PreparedStatement prep_ANSWER;
	private PreparedStatement prep_PARE;
	
	public DB_chie(){
		try{
			try {
				con = new DBManager("/Volumes/TOSHIBA EXT/result/chie_sparse_622_1000.sqlite3").createConnection();
				//con = new DBManager("/Users/masashi/Documents/news_test.sqlite3").createConnection();
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			smt = con.createStatement();
						
			smt.executeUpdate("DROP TABLE IF EXISTS QUESTION");
			smt.executeUpdate("CREATE TABLE QUESTION(ID ID, QUESTION TEXT, ANSWER TEXT);");
			
			smt.executeUpdate("DROP TABLE IF EXISTS TOPIC");
			smt.executeUpdate("CREATE TABLE TOPIC(ID ID, TOPIC TEXT);");

			smt.executeUpdate("DROP TABLE IF EXISTS WORD");
			smt.executeUpdate("CREATE TABLE WORD(ID ID, TOPIC INTEGER, WORD TEXT, P DOUBLE);");

			smt.executeUpdate("DROP TABLE IF EXISTS DOCMENT");
			smt.executeUpdate("CREATE TABLE DOCMENT(ID ID, TOPIC INTEGER, P DOUBLE);");

			smt.executeUpdate("DROP TABLE IF EXISTS ANSWERS");
			smt.executeUpdate("CREATE TABLE ANSWERS(ID ID, NUMBER INTEGER, CONTENT TEXT);");
			

			smt.executeUpdate("DROP TABLE IF EXISTS PARE");
			smt.executeUpdate("CREATE TABLE PARE(ID ID, QUESTION TEXT, ANSWER TEXT);");

			
			prep_QUESTION = con
					.prepareStatement("INSERT INTO QUESTION VALUES (?, ?, ?);");
			prep_TOPIC = con
					.prepareStatement("INSERT INTO TOPIC VALUES (?, ?);");

			prep_WORD = con
					.prepareStatement("INSERT INTO WORD VALUES (?, ?, ?, ?);");

			prep_DOCMENT = con
					.prepareStatement("INSERT INTO DOCMENT VALUES (?, ?, ?);");
			
			prep_ANSWER = con
					.prepareStatement("INSERT INTO ANSWERS VALUES (?, ?, ?);");
			
			prep_PARE = con
					.prepareStatement("INSERT INTO PARE VALUES (?, ?, ?);");

					
		}catch(SQLException e){
			
		}finally{
			
		}
	}
	
	public void insertTOPIC(int id, String str){
		try {
			prep_TOPIC.setInt(1, id);
			prep_TOPIC.setString(2, str);
			prep_TOPIC.addBatch();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPARE(int id, String str1, String str2){
		try {
			id ++;
			prep_PARE.setInt(1, id);
			prep_PARE.setString(2, str1);
			prep_PARE.setString(3, str2);
			prep_PARE.addBatch();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void insertAnswer(int id, int num ,String str){
		try {
			prep_ANSWER.setInt(1, id);
			prep_ANSWER.setInt(2, num);
			prep_ANSWER.setString(3, str);
			prep_ANSWER.addBatch();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertWORD(int v, int m, String str, Double p){
		try {
			 prep_WORD.setInt(1, v); prep_WORD.setInt(2, m);
			 prep_WORD.setString(3, str);
			 prep_WORD.setDouble(4,p);
			 prep_WORD.addBatch();
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertDOCMENT(int d, int m, Double p){
		try {
			 prep_DOCMENT.setInt(1, d); 
			 prep_DOCMENT.setInt(2, m);
			 prep_DOCMENT.setDouble(3,p);
			 prep_DOCMENT.addBatch();
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertANSWER(int d, String str, String wakati){
		try {
			 prep_ANSWER.setInt(1, d); 
			 prep_ANSWER.setString(2, str);
			 prep_ANSWER.setString(3,wakati);
			 prep_ANSWER.addBatch();
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	
	public void executePARE() throws SQLException{
		con.setAutoCommit(false);
		prep_PARE.executeBatch();
		con.commit();			
		con.close();
		smt.close();
}
	
	public void executeDB() throws SQLException{
			con.setAutoCommit(false);
			prep_TOPIC.executeBatch();
			prep_WORD.executeBatch();
			prep_DOCMENT.executeBatch();
			prep_ANSWER.executeBatch();
			prep_PARE.executeBatch();
			con.commit();			
			rs = smt.executeQuery("SELECT * FROM WORD;");
			while (rs.next()) {
				//System.out.println("ID = " + rs.getInt("ID"));
				//System.out.println("TOPIC = " + rs.getInt("TOPIC"));
				//System.out.println("WORD = " + rs.getString("WORD"));
				//System.out.println("P = " + rs.getDouble("P"));
			}
			con.close();
			smt.close();
			rs.close();
	}

}
