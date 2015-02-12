package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

public class Account {
	private String accountID;
	private String type;
	private double balance;
	private double interestRate;
	private double penalty;
	private String status;
	private Date dateOpen;
	private Date transactionDate;
	private double amount;
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private ArrayList<Customer> accountOwners = new ArrayList<Customer>();


	/**
	 * This constructor retrieves data based on a specific accountID 
	 * This data then populates an account object
	 * retrieves data from the transaction table based on accountID
	 * creates transaction objects and adds them to transactionList
	 * @param accountID - Thus unique identifier for an account
	 */
	public Account(String accountID){
		String sql = "SELECT * FROM mak213_bank1017.account WHERE accountID = '" + accountID + "'; ";
		DbUtilities db = new MySqlUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){ //populates corresponding properties of the Account object
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.balance = rs.getDouble("balance");
				this.interestRate = rs.getDouble("interestRate");
				this.penalty = rs.getDouble("penalty");
				this.status = rs.getString("status");
				this.dateOpen = new Date();
			}
		} catch (SQLException e) {
			ErrorLogger.log("Unable to retreive information");
			e.printStackTrace();
		}


		//retrieve info from transaction table
		String sql2 = "SELECT * FROM mak213_bank1017.transaction WHERE accountID ='" + accountID + "' ;";
		DbUtilities db2 = new MySqlUtilities();
		try {
			ResultSet rs2 = db2.getResultSet(sql2);
			while(rs2.next()){
				this.accountID = rs2.getString("accountID");
				this.amount = rs2.getDouble("amount");
				this.transactionDate = new Date();
				this.type = rs2.getString("type");
				this.balance = rs2.getDouble("balance");

			}
		}catch (SQLException e){
			e.printStackTrace();
			ErrorLogger.log("Could not retreive transaction information");
			ErrorLogger.log(e.getMessage());
		}finally{
			createTransaction(this.accountID, this.type, this.amount, this.balance);
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
	 * Generates a new, unique account ID
	 * Updates the account object with initial values
	 * inserts a record of the account into the MySQL account table
	 * @param accountType - description of the bank account
	 * @param initialBalance - original balance of the bank account
	 */
	public Account(String accountType, double initialBalance){
		this.accountID = UUID.randomUUID().toString();
		this.type = accountType;
		this.balance = initialBalance;
		this.interestRate = 0;
		this.penalty = 0;
		this.status = "active";
		this.dateOpen = new Date();

		String sql = "INSERT INTO mak213_bank1017.account ";
		sql += "(accountID,type,balance,interestRate,penalty,status,dateOpen) ";
		sql += " VALUES ";
		sql += "('" + this.accountID + "', ";
		sql += "'" + this.type + "', ";
		sql += this.balance + ", ";
		sql += this.interestRate + ", ";
		sql += this.penalty + ", ";
		sql += "'" + this.status + "', ";
		sql += "CURDATE());";

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
	

	/**
	 * This method adds an account owner to the array list of account owners
	 * 
	 * @param accountOwner - the owner of an account
	 */
	public void addAccountOwner(Customer accountOwner){
		accountOwners.add(accountOwner);
	}

	/**
	 * Updates account balance through subtraction
	 * Adds the transaction to the transaction list
	 * @param amount - the amount being withdrawn
	 */
	public void withdraw(double amount){
		this.balance -= amount;
		createTransaction(this.accountID, this.type, amount, this.balance);
		updateDatabaseAccountBalance();
	}

	/**
	 * Updates account balance through addition
	 * Adds the transaction to the transaction list
	 * @param amount - amount being deposited
	 */
	public void deposit(double amount){
		this.balance += amount;
		createTransaction(this.accountID, this.type, amount, this.balance);
		updateDatabaseAccountBalance();
	}

	private void updateDatabaseAccountBalance(){
		String sql = "UPDATE mak213_bank1017.account SET balance = " + this.balance + " "
				+ "WHERE accountID = '" + this.accountID + "';";

		DbUtilities db = new MySqlUtilities();
		db.executeQuery(sql);
		
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			e.printStackTrace();
		} 
	}

	/**
	 * 
	 * @param transactionID - unique identifier for a transaction
	 * @return - returns the transaction 
	 */
	private Transaction createTransaction(String transactionID){
		Transaction t = new Transaction(transactionID);
		transactionList.add(t);
		return t;
	}

	/**
	 * Creates a new account transaction
	 * Specifies an account, the account type, the amount, and balance of the account
	 * @param accountID - unique account identifies
	 * @param type - specifies the type of account
	 * @param amount - the amount of the transaction
	 * @param balance - the resulting balance of the transaction
	 * @return - returns the transaction object
	 */
	private Transaction createTransaction(String accountID, String type, double amount, double balance){
		Transaction t = new Transaction(accountID, type, amount, balance);
		transactionList.add(t);
		return t;
	}




	public String getAccountID(){
		return this.accountID;
	}

	public double getBalance(){
		return this.balance;
	}

	@Override //makes sure that a method already exists to be overridden
	public String toString(){
		return this.accountID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public ArrayList<Customer> getAccountOwners() {
		return accountOwners;
	}

	public void setAccountOwners(ArrayList<Customer> accountOwners) {
		this.accountOwners = accountOwners;
	}

	public Date getDateOpen() {
		return dateOpen;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
