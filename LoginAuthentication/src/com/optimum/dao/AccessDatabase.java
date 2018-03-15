package com.optimum.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.optimum.connection.DatabaseConnector;
import com.optimum.pojo.User;

public class AccessDatabase implements Blueprint_AccessDatabase {
	private Connection con;
	
	public AccessDatabase() {
		con = DatabaseConnector.getCon();
	}
	

	
	//Method to check Admin's credentials
	public boolean checkAdminCred(User refUser) throws SQLException{
		
		
		PreparedStatement p = con.prepareStatement("SELECT * from userdata WHERE Name=? and Password=?");  
	       
		 p.setString(1, refUser.getLogin());  
	     p.setString(2, refUser.getPassword());  
	     
	     ResultSet r= p.executeQuery();  
	       
	        if(r.next()){  
	            String str1, str2;  
	            str1=r.getString(2);  
	            str2=r.getString(7); 
	            
	            if(str1.equals("admin") && str2.equals("admin123")){  
	            	return true;
	            }
	        }
	        return false;
	}//end of checkAdminCred
	
	//Method to check User's credentials
		public boolean checkUserCred(User refUser) throws SQLException{
			
			
			PreparedStatement p = con.prepareStatement("SELECT * from userdata WHERE EmailAdd=? and Password=?");  
		       
			 p.setString(1, refUser.getLogin());  
		     p.setString(2, refUser.getPassword());  
		     
		     ResultSet r= p.executeQuery();  
		       
		        if(r.next()){  
		            String str1, str2;  
		            str1=r.getString(4);  
		            str2=r.getString(7); 
		            
		            if(str1.equals(refUser.getLogin()) && str2.equals(refUser.getPassword())){  
		            	return true;
		            }
		        }
		        return false;
		}//end of checkUserCred
		
		//					---End of LoginCheck methods---			  \\
		
		//---------------------------------------------------------------------------------------------------\\
		//----------------------------------------------------------------------------------------------------\\
		
		
		//					---Start of User based methods---			\\
		
		public void incrementUserAttempt(User refUser) {
			
			try {
			PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET NumOfAttempts=NumOfAttempts + 1 WHERE EmailAdd=?");
			
			stmnt.setString(1, refUser.getLogin());
			stmnt.executeUpdate();
			
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
		
		public boolean accountLockNotif(User refUser) throws SQLException {
			
				PreparedStatement stmnt = con.prepareStatement("SELECT NumOfAttempts FROM userdata WHERE EmailAdd=?");
				
				stmnt.setString(1, refUser.getLogin());
				
				ResultSet r = stmnt.executeQuery();
				
				if(r.next()) {
					int value;
					value = r.getInt("NumOfAttempts");
					
					if(value == 3) {
						lockAccount(refUser.getLogin()); //Method sets status to 1(LOCK) when NumOfAttempts hit 3
						return true;
					}
				}
				return false;
				
			}
		
		public void lockAccount(String id) throws SQLException{	//Method to set lock(1) value
			PreparedStatement sts = con.prepareStatement("UPDATE userdata SET FirstLog=1 WHERE EmailAdd=?");
			
			sts.setString(1, id);
			
			sts.executeUpdate();
		
		}
		
		public void firstLogin(User refUser) {
			try {
			PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET FirstLog=FirstLog+1 WHERE EmailAdd=?");
			
			stmnt.setString(1, refUser.getLogin());
			stmnt.executeUpdate();
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		
		public boolean checkFirstLogin(User refUser) throws SQLException{
			
			PreparedStatement stmnt = con.prepareStatement("SELECT FirstLog FROM userdata WHERE EmailAdd=?");
			stmnt.setString(1, refUser.getLogin());
			
			ResultSet r = stmnt.executeQuery();
			
			if(r.next()) {
				int value;
				value = r.getInt("FirstLog");
				
				if(value == 1) {
					return true;
				}
			}
			return false;
		}
		
		public boolean checkPass(String pass,String email) throws SQLException{
			
			PreparedStatement stmnt = con.prepareStatement("SELECT Password FROM userdata WHERE EmailAdd=?");
			stmnt.setString(1, email);
			
			ResultSet r = stmnt.executeQuery();
			
			if(r.next()) {
				String value;
				value = r.getString("Password");
				
				if(value.equals(pass)) {
					return true;
				}
			}
			return false;
		}
		
		public void setNewPassword(String ID, String newPass) throws SQLException{
			
			PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET Password=? WHERE EmailAdd=?");
			
			stmnt.setString(1, newPass);
			stmnt.setString(2, ID);
			
			stmnt.executeUpdate();
		}
		
		public String getSecurityQuestion(String email) throws SQLException{
			String getQ = "";
			
			PreparedStatement stmnt = con.prepareStatement("SELECT SecurityQuestion FROM userdata WHERE EmailAdd=?");
			stmnt.setString(1, email);
			
			ResultSet rs = stmnt.executeQuery();
			
			while(rs.next()) {
				getQ = rs.getString("SecurityQuestion");
			}
			return getQ;
		}
		
		public void setSecurityQuestion(String ques, String emailID) throws SQLException{
			PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET SecurityQuestion=? WHERE EmailAdd=?");
			stmnt.setString(1, ques);
			stmnt.setString(2, emailID);
			
			stmnt.executeUpdate();
		}
		
		public void setSecurityAnswer(String ans, String emailID) throws SQLException{
			
			PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET SecurityAnswer=? WHERE EmailAdd=?");
			
			stmnt.setString(1, ans);
			stmnt.setString(2, emailID);
			
			stmnt.executeUpdate();
		}
		
		public boolean getSecurityAnswer(String answer,String email) throws SQLException{
			String A = "";
			
			PreparedStatement stmnt = con.prepareStatement("SELECT SecurityAnswer FROM userdata WHERE EmailAdd=?");
			stmnt.setString(1, email);
			
			ResultSet rs = stmnt.executeQuery();
			
			if(rs.next()) {
				A = rs.getString("SecurityAnswer");
				if(A.equals(answer)) {
					return true;
				}
			}
			return false;
		}

	//						---End of User based methods---					\\
		
		
		//----------------------------------------------------------------------------\\
	   //------------------------------------------------------------------------------\\

	//                          -----Start of Admin methods----- 				//
		
		//These are the methods inside the admin menu
	
	public void returnUserList() throws SQLException {		//Returns all entries for admin
		
		Statement states = con.createStatement();
		ResultSet rs = states.executeQuery("SELECT * FROM userdata");
		
		while(rs.next()) {
			System.out.print (rs.getString("Name") + "\t");
			System.out.print(rs.getString("NRIC")  + "\t");
			System.out.print(rs.getString("EmailAdd")  + "\t");
			System.out.print(rs.getInt("Mobile")  + "\t");
			System.out.print(rs.getDate("DOB")  + "\t");
			System.out.print(rs.getString("Password")  + "\t");
			System.out.print(rs.getString("Role") + "\t");
			System.out.print(rs.getString("SecurityQuestion")  + "\t");
			System.out.print(rs.getString("SecurityAnswer")  + "\t");
			System.out.print(rs.getInt("FirstLog")  + "\t");
			System.out.print(rs.getInt("Status")  + "\t");
			System.out.println(rs.getInt("NumOfAttempts")  + "\t");
		}
		System.out.println("");
	}
	
	public void unlockAccount(String email) throws SQLException{		//Method to unlock user account by resetting counters
		PreparedStatement stmnt = con.prepareStatement("UPDATE userdata SET Status=0,NumOfAttempts=0 WHERE EmailAdd=?");
		
		stmnt.setString(1, email);
		
		stmnt.executeUpdate();
	}
	
	//						-----End of Admin methods-----					//
	
	
	//					-----Start of Registration checker methods-----   			//
	
	public boolean ifNRICExists(String NRIC) throws SQLException{		//Check if NRIC exists in database
		Statement stmnt = con.createStatement();
		ResultSet rs = stmnt.executeQuery("SELECT * FROM userdata where NRIC=\'" + NRIC + "\'");
		
		while(rs.next()) {
			if(rs.getString("NRIC") != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean ifEmailExists(String email) throws SQLException{		//Check if email exists in database
		Statement stmnt = con.createStatement();
		ResultSet rs = stmnt.executeQuery("SELECT * FROM userdata where EmailAdd=\'" + email + "\'");
		
		while(rs.next()) {
			if(rs.getString("EmailAdd") != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean ifMobileNoExists(int num) throws SQLException{		//Check if mobileNo exists in database
		Statement stmnt = con.createStatement();
		ResultSet rs = stmnt.executeQuery("SELECT * FROM userdata where Mobile=\'" + num + "\'");
		
		while(rs.next()) {
			if(rs.getString("Mobile") != null) {
				return true;
			}
		}
		return false;
	}
	
	public void insertIntoTable(String name, String nric, String dob,String email,int mobile,String pass) {
	
		String year = dob.substring(6);
		String month = dob.substring(3, 5);
		String day = dob.substring(0, 2);
		
		String sqlDate = year + "-" + month + "-" + day;
		
		try {
			
		String query = "INSERT INTO userdata(Name, NRIC, EmailAdd, Mobile, DOB, Password, Role, SecurityQuestion, SecurityAnswer, FirstLog, Status, NumOfAttempts)values (?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmnt = con.prepareStatement(query);
		
		stmnt.setString(1, name);
		stmnt.setString(2, nric);
		stmnt.setString(3, email);
		stmnt.setInt(4, mobile);
		stmnt.setDate(5, Date.valueOf(sqlDate));
		stmnt.setString(6, pass);
		stmnt.setString(7, "User");
		stmnt.setString(8, "");
		stmnt.setString(9, "");
		stmnt.setInt(10,0);
		stmnt.setInt(11, 0);
		stmnt.setInt(12, 0);
		
		stmnt.executeUpdate();
		
		
		sendTempPassword(name,pass,email);
		System.out.println("Registration Successful.\n");
		
	}catch(Exception e) {
		System.out.println(e);
	}
}
	
	// 								------End of Registration Checker methods-----							//
	
	//								-----Send temp password to email method------							//
	
	public void sendTempPassword(String name, String password, String email) {
		
		String to = email;
		String from = "optimum.batch5@gmail.com";
		String emailPass = "Optimum2018";
		
		//Getting session object
			Properties props = System.getProperties();
				props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
				props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
				props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
				props.put("mail.smtp.port", "465"); // SMTP Port
				
				Authenticator auth = new Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from,emailPass);
			}
		};
		Session session = Session.getDefaultInstance(props, auth); 
			//compose the message
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			msg.setSubject("Temp password");
			msg.setText("Hello " + name + "! The following is your temporary password: " + password);
			
			//Send message
			Transport.send(msg);
			System.out.println("Temporary password has been sent to " + email + ".");
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
}
