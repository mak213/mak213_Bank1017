package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

public class Customer {
	private String customerID;
	private String firstName;
	private String lastName;
	private String ssn;
	private String streetAddress;
	private String city;
	private String state;
	private String zip;
	private String loginName;
	private int pin;

	/**
	 * This constructor retrieves data for the provided customer ID and updates the appropriate values of the customer class
	 * @param customerID - this field is a unique identifier for a customer
	 */
	public Customer(String customerID){
		String sql = "SELECT * FROM mak213_bank1017.customer WHERE customerID = '"+ customerID +"' ;";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql); 
			while(rs.next()){
				this.customerID = customerID;
				this.lastName = rs.getString("lastName");
				this.firstName = rs.getString("firstName");
				this.ssn = rs.getString("ssn");
				this.streetAddress = rs.getString("streetAddress");
				this.city = rs.getString("city");
				this.state = rs.getString("state");
				this.zip = rs.getString("zip");
				this.loginName = rs.getString("loginName");
				this.pin = rs.getInt("pin");
			}
		}catch (SQLException e){
			e.printStackTrace();
			ErrorLogger.log("Unable to retrieve customer information");
			ErrorLogger.log(e.getMessage());
		}
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
		} 


	}

	/**
	 * This constructor generates a new customer ID, updates the appropriate properties of the customer class, and inserts a record into the 
	 * MYSQL customer table
	 * @param lastName - customers last name
	 * @param firstName - customers first name
	 * @param ssn - customer's social security number
	 * @param loginName - customer's bank login name
	 * @param pin - customer's personal identification number
	 */
	public Customer(String lastName, String firstName, String ssn, String loginName, int pin, String streetAddress, String city, String state, String zip){
		this.customerID = UUID.randomUUID().toString();
		this.lastName = lastName;
		this.firstName = firstName;
		this.ssn = ssn;
		this.loginName = loginName;
		this.pin = 0;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zip = zip;

		String sql2 = "INSERT INTO mak213_bank1017.customer";
		sql2 += "(customerID, lastName, firstName, ssn, loginName, pin)";
		sql2 += " VALUES ";
		sql2 += "('" + this.customerID + "', ";
		sql2 += "'" + this.lastName + "', ";
		sql2 += "'" + this.firstName + "', ";
		sql2 += "'" + this.ssn + "', ";
		sql2 += "'" + this.loginName + "', ";
		sql2 += this.pin + ") ; ";

		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql2);
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
			
		} 


	}

	public String getCustomerID() {
		return customerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getLoginName() {
		return loginName;
	}

	public int getPin() {
		return pin;
	}

}
