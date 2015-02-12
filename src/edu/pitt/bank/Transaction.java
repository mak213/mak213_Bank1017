package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

public class Transaction {
	private String transactionID;
	private String accountID;
	private String type;
	private double amount;
	private double balance;
	private Date transactionDate; 


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public String getAccountID() {
		return accountID;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Retrieves data based on a provided transaction ID
	 * Updates the properties of the transaction class
	 * @param transactionID - A unique ID for a banking transaction
	 */
	public Transaction(String transactionID){
		String sql = "SELECT * FROM mak213_bank1017.transaction "; 
		sql += "WHERE transactionID = '" + transactionID + "'"; 
		System.out.println(sql);
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql); 
			while(rs.next()){
				this.transactionID = rs.getString("transactionID");
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.amount = rs.getDouble("amount");
				this.balance = rs.getDouble("balance");
				this.transactionDate = new Date();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			ErrorLogger.log("Could not retrieve transaction data");
			ErrorLogger.log(ex.getMessage());
			
		}
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			e.printStackTrace();
		} 

	}

	/**
	 * This overloaded method updates the MYsql table with the information that occurs with a banking transaction
	 * It adds a record to the table as opposed to simply looking at the table like the method above
	 * @param accountID - The number that identifies an account
	 * @param type - The type of banking account
	 * @param amount - The amount of money being moved in the transaction
	 * @param balance - The money in the account before and after the transaction
	 */
	public Transaction(String accountID, String type, double amount, double balance){
		this.transactionID = UUID.randomUUID().toString();
		this.type = type;
		this.amount = amount;
		this.accountID = accountID;
		this.balance = balance;

		String sql = "INSERT INTO mak213_bank1017.transaction ";
		sql += "(transactionID, accountID, amount, transactionDate, type, balance) ";
		sql += " VALUES ";
		sql += "('" + this.transactionID + "', ";
		sql += "'" + this.accountID + "', ";
		sql += amount + ", ";
		sql += "CURDATE(), ";
		sql += "'" + this.type + "', ";
		sql += balance + ");";

		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			ErrorLogger.log(e.getMessage());
			e.printStackTrace();
		} 

	}
}
