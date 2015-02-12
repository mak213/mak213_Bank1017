package edu.pitt.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JButton;

import edu.pitt.bank.Customer;
import edu.pitt.bank.Security;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.DbUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;

public class LoginUI {

	private JFrame frmBankLogin;
	private JTextField txtLogin;
	private JTextField txtPassword;
	private JTextField txtLoginName;
	private JLabel lblPassword;
	private JTextField txtPassWrd;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frmBankLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBankLogin = new JFrame();
		frmBankLogin.setTitle("Bank 1017 Login");
		frmBankLogin.setBounds(100, 100, 450, 300);
		frmBankLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankLogin.getContentPane().setLayout(null);

		txtLoginName = new JTextField();
		txtLoginName.setBounds(111, 45, 293, 22);
		frmBankLogin.getContentPane().add(txtLoginName);
		txtLoginName.setColumns(10);

		JLabel lblLoginName = new JLabel("Login Name:");
		lblLoginName.setBounds(27, 48, 72, 16);
		frmBankLogin.getContentPane().add(lblLoginName);

		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(27, 95, 72, 16);
		frmBankLogin.getContentPane().add(lblPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setSelected(true);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{

					String loginName = txtLoginName.getText();
					int pin = Integer.parseInt(txtPassword.getText()); //handle errors that happen with text in the pin field
					Security s = new Security();
					Customer c = s.validateLogin(loginName, pin);
					if( c != null){
						AccountDetailsUI ad = new AccountDetailsUI(c);
						frmBankLogin.setVisible(false);
					}else{
						JOptionPane.showMessageDialog(null, "Invalid Login");
					}
				}catch(Exception ex){
					ErrorLogger.log("Unable to log in");
					ErrorLogger.log(ex.getMessage());
				}
			}
		});
		btnLogin.setBounds(198, 127, 97, 25);
		frmBankLogin.getContentPane().add(btnLogin);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
				
			}
		});
		btnExit.setBounds(307, 127, 97, 25);
		frmBankLogin.getContentPane().add(btnExit);

		txtPassword = new JTextField();
		txtPassword.setBounds(111, 89, 293, 22);
		frmBankLogin.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);


	}
}
