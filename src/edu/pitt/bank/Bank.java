package edu.pitt.bank;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;

public class Bank {

	private String accountID;
	private String customerID;
	private ArrayList<Account> accountList = new ArrayList<Account>();
	private ArrayList<Customer> customerList = new ArrayList<Customer>();

	
	/**
	 * Default constructor for the bank class. 
	 * Loads accounts and sets account owners for the bank
	 */
	public Bank(){

		loadAccounts();
		setAccountOwners();

	}

	/**
	 * Retrieves a list of all accounts from the database
	 * Creates a new account object for each account that is retrieved from the DB
	 * Adds the account object to the accountList array list
	 */
	private void loadAccounts(){
		String sql = "SELECT * FROM account";
		DbUtilities db = new MySqlUtilities();
		

		try {
			ResultSet rs = db.getResultSet(sql);

			while(rs.next()){
				//set the account object here
				this.accountID = rs.getString("accountID");
				Account acct = new Account("accountID");
				accountList.add(acct);
			}

		}catch (SQLException e) {
			e.printStackTrace();
			ErrorLogger.log("Error with loading Accounts");
			ErrorLogger.log(e.getMessage());
		}
	}

	/**
	 * Searches the accountList array list for an account with a specific accountID
	 * 
	 * @param accountID - unique identifier for an account
	 * @return - returns an account object from the array list
	 */
	public Account findAccount(String accountID){

		for(int i = 0; i < accountList.size(); i++ )
		{
			if (accountList.get(i).getAccountID().equals("accountID")){
				return accountList.get(i);
			}
		}

		return null;
	}
	/**
	 * Searches the customerList array list for a customer with a specific customerID
	 * 
	 * @param customerID - unique identifier for a customer
	 * @return - returns a customer object from the array list
	 */
	public Customer findCustomer(String customerID){
		
		for(int i = 0; i < customerList.size(); i++ )
		{
			if (customerList.get(i).getCustomerID().equals("customerID")){
				return customerList.get(i);
			}
		}
		
		return null;
	}
	/**
	 * Retrieves a list of all accounts and corresponding accountOwners from the database. 
	 * 
	 */
	private void setAccountOwners() {

		String sql = "SELECT * FROM account JOIN customer_account ON accountId = fk_accountId JOIN customer ON fk_customerId = customerId; "

			+ "SELECT accountID, type, balance, interestRate, penalty, status, dateOpen, customerID, lastName, "
			+ "firstName, ssn, streetAddress, city, state, zip, loginName, pin FROM account "
			+ "JOIN customer_account ON accountId = fk_accountId "
			+ "JOIN customer ON fk_customerId = customerId;";

		DbUtilities db = new MySqlUtilities();

		try {
			ResultSet rs = db.getResultSet(sql);

			while(rs.next()){
				this.customerID = rs.getString("customerID");
				this.accountID = rs.getString("accountID");

				Customer cust = new Customer("customerID");
				Account acct = new Account(accountID);

				customerList.add(cust);
				acct.addAccountOwner(cust);


			}

		}catch (SQLException ex) {
			ex.printStackTrace();
			ErrorLogger.log("Could not update account owner");
			ErrorLogger.log(ex.getMessage());
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
