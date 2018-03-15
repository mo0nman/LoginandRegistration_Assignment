package com.optimum.controller;

import java.sql.SQLException;
import java.util.Scanner;

import com.optimum.dao.AccessDatabase;
import com.optimum.pojo.User;

public class LoginLogic {

	 private Scanner ss = new Scanner(System.in);
	 private AccessDatabase AD = new AccessDatabase();
	 private String userName, NRIC, email, dob, tempPass,mobile;	//Used for registration
	 private String getUserID;		//For first time login purposes
	 private String getCompletedUserID;  //For completed user purposes
	 private String fpEmail;//Used for "Forget Password" section;
	 private int newMob;// For parsing the String mobile into an integer to store in database
	 
	 
	 
	 //													----LOGIN start----
	 
	public void login() throws SQLException{			//Login checks for both user and admin
		
		String pattern = "^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){2}\\.[a-z]{2,3}$";
		String name;
			
		while(true) {
				System.out.print("\nLogin ID: ");
				 name = ss.next();
				 if(!name.matches(pattern) && !name.equals("admin")) {
					 System.out.println("Please enter a valid LoginID (Email Address).");
				 }else {
					 break;
				 }
			}
			
			System.out.print("Password: ");
			String pass = ss.next();
			
			User refUser = new User(name,pass);
			
			if(AD.checkAdminCred(refUser) == true) {			//checks for admin login
				System.out.println("\nWelcome admin");
				AdminAccess();
			}
			else if(AD.accountLockNotif(refUser) == true) {			//checks for locked account
				System.out.println("Your account has been locked. Please approach an admin.");
				login();
			}
			else if(AD.checkFirstLogin(refUser) == false && AD.checkUserCred(refUser) == true) {
				this.getUserID = refUser.getLogin();
				System.out.println("\nWelcome " + refUser.getLogin());
				AD.firstLogin(refUser);
				tempPass();
			}
			else if(AD.checkUserCred(refUser) == true && AD.checkFirstLogin(refUser) == true){
				this.getCompletedUserID = refUser.getLogin();
				System.out.println("\nWelcome " + refUser.getLogin());
				UserAccess();
			}else {
				System.out.println("Invalid login attempt. Please try again.");
				AD.incrementUserAttempt(refUser);
				login();
			}
		}
	
	//												-----LOGIN end-----
	
	
	//											---Forget Password start----
	
	public void forgetPassword() throws SQLException{
		
		System.out.print("Please enter your LoginID: ");
		this.fpEmail = ss.next();
		
		if(AD.ifEmailExists(fpEmail) == true) {
			checkSecurityQandA();
		}else {
			System.out.println("Please enter a valid LoginID(email add).\n");
			forgetPassword();
		}
		
	}
	
	public void checkSecurityQandA() throws SQLException{
		System.out.println(AD.getSecurityQuestion(this.fpEmail));
		
		System.out.println("Enter your answer: ");
		String answer = ss.next();
		
		if(AD.getSecurityAnswer(answer, fpEmail) == true) {
			setNewPass();
		}else {
			System.out.println("Your answer is invalid. Please try again.");
		}
	}
	
	public void setNewPass() throws SQLException{
		
			System.out.print("\nPlease key in your new password: ");
			String newPassword = ss.next();
			
			System.out.print("Please re-key in your new password: ");
			String nPCheck = ss.next();
			
			if(!nPCheck.equals(newPassword)) {
				System.out.println("Your passwords do not match. Please try again.");
				setNewPass();
			}else {
				System.out.println("Password change successful.");
				AD.setNewPassword(this.fpEmail, nPCheck);
				setNewSecurityQuestions();
			}
	}
	
	public void setNewSecurityQuestions() throws SQLException{
		
		String question1 = "What is your mother's middle name?";
		String question2 = "Who is your childhood hero?";
		String question3 = "What is your favourite color?";
		
		
			System.out.println("\nSelect your question..");
			System.out.println(question1);
			System.out.println(question2);
			System.out.println(question3);
			
			System.out.println("Enter choice 1, 2 or 3");
			String choice4 = ss.next();
		
			switch(choice4) {
			case "1":
				//System.out.println("CHECKER" + this.fpEmail);
				AD.setSecurityQuestion(question1, this.fpEmail);
				break;
			case "2":
				AD.setSecurityQuestion(question2, this.fpEmail);
				break;
			case "3":
				AD.setSecurityQuestion(question3, this.fpEmail);
				break;
				
			default:
					System.out.println("You have entered an invalid option. Please try again.");
					setNewSecurityQuestions();
					break;
			}
			
			System.out.print("Enter the answer to your security question: ");
			String answer = ss.next();
		
			AD.setSecurityAnswer(answer, this.fpEmail);
			System.out.println("Update Successful.");
	}

	
	//										---ADMIN MENU---				//			
	
	public void AdminAccess() throws SQLException {		
	
	OUTER:
		while(true) {
			System.out.println("1. Register New User");
			System.out.println("2. View User List");
			System.out.println("3. Logout");
			
			int choice2 = Integer.parseInt(ss.next());
			
			switch(choice2) {
			case 1:
				UserRegistration();
				break;
				
			case 2:
				viewAndUnlock();
				break;
				
			case 3:
				break OUTER;
				
			default:
					System.out.println("Please enter a valid choice");
			}
		}
	}	
	//										---End of Admin Menu---			//
	
	
	//										---FIRST USER LOG IN---			//
	
	public void tempPass() throws SQLException{
		
		
		System.out.print("Enter temp pass: ");
		String tempP = ss.next();
		
		if(AD.checkPass(tempP, this.getUserID) == true) {
			System.out.print("Please key in your new password: ");
			String newPassword = ss.next();
			
			System.out.print("Please re-key in your new password: ");
			String nPCheck = ss.next();
			
			if(!nPCheck.equals(newPassword)) {
				System.out.println("Your passwords do not match. Please try again.");
				tempPass();
			}else {
				System.out.println("Password change successful.");
				AD.setNewPassword(this.getUserID, nPCheck);
				setSecurityQuestions();
			}
		}else {
			System.out.println("You have entered an invalid temporary password. Please try again.");
			tempPass();
		}
	}
	
	public void setSecurityQuestions() throws SQLException{
		
		String question1 = "What is your mother's middle name?";
		String question2 = "Who is your childhood hero?";
		String question3 = "What is your favourite color?";
		
		
			System.out.println("\nSelect your question..");
			System.out.println(question1);
			System.out.println(question2);
			System.out.println(question3);
			
			System.out.println("Enter choice 1, 2 or 3");
			String choice = ss.next();
		
			switch(choice) {
			case "1":
				AD.setSecurityQuestion(question1, this.getUserID);
				break;
			case "2":
				AD.setSecurityQuestion(question2, this.getUserID);
				break;
			case "3":
				AD.setSecurityQuestion(question3, this.getUserID);
				break;
				
			default:
					System.out.println("You have entered an invalid option. Please try again.");
					setSecurityQuestions();
					break;
			}
			
			System.out.print("Enter the answer to your security question: ");
			String answer = ss.next();
		
			AD.setSecurityAnswer(answer, this.getUserID);
			System.out.println("Update Successful.");
			login();
		
	}
	
	//										---END FIRST USER LOG IN---		//
	
	
	//										---Start of User Menu---		//
	
	public void UserAccess() throws SQLException{
	
		
			System.out.println("Logout?(yes/no): ");
			String ans = ss.next();
			
			if(ans.equals("yes")){
				System.out.println("Thank You!");
				login();
			}else {
				UserAccess();
			}
	}
	
	
						//							-----Start of UserRegistration Method-----				//
	
	public void UserRegistration() throws SQLException{		//Registration step 1/5
		String pattern = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
		
		System.out.println("Enter Name: ");
		this.userName = ss.next();
		
		if(this.userName.matches(pattern)) {
			NRICcheck();
		}else {
			System.out.println("Please enter a proper name.");
			UserRegistration();
		}
	}
	
	public void NRICcheck() throws SQLException {			//Registration step 2/5.
		String pattern = "^[a-zA-Z][0-9]{7}[a-zA-Z]$";
		
		System.out.print("Enter NRIC: ");
		this.NRIC = ss.next();
		
		  if(AD.ifNRICExists(this.NRIC) == false && this.NRIC.matches(pattern)) {
			  DOBcheck();
		}else {
			System.out.println("NRIC either exists or is an invalid input. Please try again.");
			NRICcheck();
		}
		
	}
	
	public void DOBcheck() throws SQLException { 		//Registration step 3/5
		
		String pattern = "^(((0[1-9]|[12][0-9]|3[01])[- /.](0[13578]|1[02])|(0[1-9]|[12][0-9]|30)[- /.](0[469]|11)|(0[1-9]|1\\d|2[0-8])[- /.]02)[- /.]\\d{4}|29[- /.]02[- /.](\\d{2}(0[48]|[2468][048]|[13579][26])|([02468][048]|[1359][26])00))$";
		
		System.out.print("Enter date of birth (dd-mm-yyyy): ");
		this.dob = ss.nextLine();
		
		if(this.dob.matches(pattern)) {	
			emailCheck();
	        }else {
			System.out.println("Please enter a valid date.");
			DOBcheck();
		}
	}
	
	public void emailCheck() throws SQLException{		//Registration step 4/5
		String pattern = "^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){2}\\.[a-z]{2,3}$";
		
		System.out.print("Enter e-mail address: ");
		this.email = ss.nextLine();
		
		if(this.email.matches(pattern) && AD.ifEmailExists(this.email) == false) {
			mobileCheck();
		}else {
			System.out.println("Email either exists or is an invalid input. Please try again.");
			emailCheck();
		}
		
	}
	
	public void mobileCheck() throws SQLException {		//Registration step 5/5
		
		String pattern = "^[0-9]{8}$";
		
		System.out.print("Enter mobile number: ");
		this.mobile = ss.nextLine();
		
		if(this.mobile.matches(pattern)) {
			newMob = Integer.parseInt(this.mobile);
		}else {
			System.out.println("Please re-enter a valid 8 digit mobile number.");
			mobileCheck();
		}
		
				if(AD.ifMobileNoExists(newMob) == false) {
					makeTempPass();
					AD.insertIntoTable(this.userName, this.NRIC,this.dob,this.email,this.newMob,this.tempPass);
				}else {
					System.out.println("Mobile number exists. Please try again.");
					mobileCheck();
				}
		}
	
	
	public void makeTempPass() throws SQLException{		
		this.tempPass = this.NRIC.substring(1, 5) + this.mobile.substring(4);// Selects first 4 digits of NRIC and last 4 digits of Mobile number.
	}
	
				//								-----End of UserRegistration Method-----				//
	
	
			//								----Start of view and unlock user list method----				//
	
	public void viewAndUnlock() throws SQLException{
		String nxt = "";
		String pattern = "^[a-z]$";
		
		AD.returnUserList();
		
		System.out.print("Do you wish to unlock an account?(y/n)");
		nxt = ss.next();
		
		if(nxt.matches(pattern) && nxt.equals("y")) {
			unlockAcc();
		}
		else if(nxt.matches(pattern) && nxt.equals("n")) {
			AdminAccess();
		}else {
			System.out.println("You have entered an invalid option. Please try again.\n");
			viewAndUnlock();
		}
		
	}
	
	public void unlockAcc() throws SQLException{
		
		String pattern="^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){2}\\.[a-z]{2,3}$";
		
		System.out.println("Which account do you want to unlock?");
		String email = ss.next();
		
		if(AD.ifEmailExists(email) == true && email.matches(pattern)) {
			AD.unlockAccount(email);
			System.out.println("Unlock successful.\n");
			viewAndUnlock();
		}else {
			System.out.println("Please enter a valid e-mail.\n");
			unlockAcc();
		}
	}
	
	//								----Start of view and unlock user list method----				//
}
