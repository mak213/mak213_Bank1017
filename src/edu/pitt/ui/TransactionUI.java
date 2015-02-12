package edu.pitt.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransactionUI {

	private JFrame frame;
	private JScrollPane transactionPane;
	private JTable tblTransactions;
	private Account userAccount;

	/**
	 * Create the application.
	 */
	public TransactionUI(Account acct) {
		userAccount = acct;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 274);
		frame.setTitle("Account Transactions");		
		
		transactionPane = new JScrollPane();
		frame.getContentPane().add(transactionPane);
		DbUtilities db = new MySqlUtilities();
		
		String[] cols = {"Account Number", "Amount", "Transaction Date", "Transaction Type", "Balance"  };
		String sql = "SELECT accountID, amount, transactionDate, type, balance FROM transaction WHERE amount <> 0 ORDER BY transactionDate DESC ;";

		try{
			DefaultTableModel transactionList = db.getDataTable(sql, cols); 
			tblTransactions = new JTable(transactionList);
			tblTransactions.setFillsViewportHeight(true);
			transactionPane.getViewport().add(tblTransactions);
			tblTransactions.setShowGrid(true);
			tblTransactions.setGridColor(Color.GRAY);

		} catch (SQLException e){
			e.printStackTrace();
			ErrorLogger.log("Unable to display table");
		}

	}

}
