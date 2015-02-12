package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

public class Security {

private ArrayList <Customer> userGroups = new ArrayList <Customer>();
private String userID;
	
	/**
	 * This method retrieves a record from the customer table based on the login name and pin
	 * Creates and returns a customer object if a user is found
	 * 
	 * @param loginName - unique identifier for a user to enter the system
	 * @param pin - personal identification number. Unique to each login name
	 * @return
	 */
	public Customer validateLogin(String loginName, int pin){
		String sql = "SELECT * FROM mak213_bank1017.customer WHERE loginName = '"+ loginName +"' AND pin = '"+ pin +"' ;";
		Customer cust = null;
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			if(rs.next()){
				cust = new Customer(rs.getString("customerID"));//customerID matches
			}else{
				return null;
			}
		}catch(SQLException e){
			ErrorLogger.log("Could not validate login");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
		}
		
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
			
		} 

		return cust;
	}
	
	/**
	 * This method should pull an array list containing all groups to which a logged in user belongs.
	 *  
	 * @param userID - customer identifier from the user_permissions table
	 */
	public void listUserGroups(String userID){
		
		String sql = "SELECT groupID FROM user_permissions WHERE groupOrUserID = '"+ userID +"'";
		
		DbUtilities db = new MySqlUtilities();
		ArrayList<String> groupID = null;
		try{
			ResultSet rs = db.getResultSet(sql);
			while(rs.next() ){
				groupID.add(rs.getString("groupID"));
			}
		}catch(SQLException e){
			ErrorLogger.log("Could not add user group ID's");
			ErrorLogger.log(e.getMessage());
		}
		
		try{
			
			String sql2 = "SELECT * FROM groups WHERE groupID = '"+ groupID +"';";
			
			DbUtilities db2 = new MySqlUtilities();
			
			ResultSet rs2 = db2.getResultSet(sql2);
			
			while(rs2.next() ){	
				groupID.add(rs2.getString("groupID"));
			}
		
			
		}catch(Exception exc){
			ErrorLogger.log("Could not list user groups");
			ErrorLogger.log(exc.getMessage());
		}
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
			
		} 
		
	}
}



