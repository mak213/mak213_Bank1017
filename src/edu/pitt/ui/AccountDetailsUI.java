package edu.pitt.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JComboBox;

import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

public class AccountDetailsUI {

	private JFrame frame;


	JTextArea txtDebug;
	JComboBox cboAccounts; 
	private JLabel lblYourAccounts;
	private JTextArea txtAcctDescription;
	private JTextArea txtAcctDetails;
	private JTextField txtAmount;
	private JLabel lblAmount;
	private Customer accountOwner;




	public AccountDetailsUI(Customer c) {
		accountOwner = c;
		initialize();

	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 337);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		cboAccounts = new JComboBox();
		cboAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				Account selectedAccount = (Account) cboAccounts.getSelectedItem();
				
				txtAcctDetails.setText("Account Type: " + selectedAccount.getType() + "\nBalance: $" + selectedAccount.getBalance() + 
										"\nInterest Rate: " + selectedAccount.getInterestRate() + "\nPenalty: $" + selectedAccount.getPenalty() );
				
				txtDebug.setText("Account: " + selectedAccount.getAccountID() + " = " + selectedAccount);
			}
		});
		cboAccounts.setBounds(141, 63, 449, 22);
		frame.getContentPane().add(cboAccounts);

		lblYourAccounts = new JLabel("Your accounts:");
		lblYourAccounts.setVerticalAlignment(SwingConstants.TOP);
		lblYourAccounts.setBounds(46, 66, 96, 22);
		frame.getContentPane().add(lblYourAccounts);

		txtDebug = new JTextArea();
		txtDebug.setBounds(46, 13, 543, 37);
		frame.getContentPane().add(txtDebug);

		txtAcctDetails = new JTextArea();
		txtAcctDetails.setBounds(46, 107, 233, 112);
		frame.getContentPane().add(txtAcctDetails);

		txtAmount = new JTextField();
		txtAmount.setBounds(467, 107, 123, 22);
		frame.getContentPane().add(txtAmount);
		txtAmount.setColumns(10);

		lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(406, 104, 71, 29);
		frame.getContentPane().add(lblAmount);

		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					double amount = Double.parseDouble(txtAmount.getText());
					Account acct = (Account) cboAccounts.getSelectedItem();
					acct.withdraw(amount);
					
					txtAcctDetails.setText("Account Type: " + acct.getType() + "\nBalance: $" + acct.getBalance() + 
							"\nInterest Rate: " + acct.getInterestRate() + "\nPenalty: $" + acct.getPenalty() );
					
				}catch(Exception ex){
					ErrorLogger.log("Withdrawal could not be processed");
					ErrorLogger.log(ex.getMessage());
				}
			}
		});
		btnWithdraw.setBounds(493, 142, 97, 25);
		frame.getContentPane().add(btnWithdraw);

		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					double amount = Double.parseDouble(txtAmount.getText());
					Account acct = (Account) cboAccounts.getSelectedItem();
					acct.deposit(amount);
					
					txtAcctDetails.setText("Account Type: " + acct.getType() + "\nBalance: $" + acct.getBalance() + 
							"\nInterest Rate: " + acct.getInterestRate() + "\nPenalty: $" + acct.getPenalty() );
					
				}catch(Exception ex){
					ErrorLogger.log("Deposit could not be processed");
					ErrorLogger.log(ex.getMessage());
					
				}
			}
		});
		btnDeposit.setBounds(394, 142, 83, 25);
		frame.getContentPane().add(btnDeposit);

		JButton btnShowTransactions = new JButton("Show Transactions");
		btnShowTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					
					Account acct = (Account) cboAccounts.getSelectedItem();
					
					if( acct != null){
						TransactionUI tui = new TransactionUI(acct);
					}else{
						JOptionPane.showMessageDialog(null, "No Transactions to show");
					}
				}catch(Exception ex){
					ErrorLogger.log("Unable to display transactions");
					ErrorLogger.log(ex.getMessage());
				}
				
			}
		});
		btnShowTransactions.setBounds(323, 194, 154, 25);
		frame.getContentPane().add(btnShowTransactions);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		
		btnExit.setBounds(493, 192, 97, 25);
		frame.getContentPane().add(btnExit);

		DbUtilities db = new MySqlUtilities();
		String sql = "SELECT accountID FROM account";
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				Account acct = new Account(rs.getString("accountID"));
				cboAccounts.addItem(acct);
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			db.closeDbConnection();
		} catch (SQLException e) {
			ErrorLogger.log("Could not close connection");
			e.printStackTrace();
		} 
		frame.setVisible(true);
		
	}
}
