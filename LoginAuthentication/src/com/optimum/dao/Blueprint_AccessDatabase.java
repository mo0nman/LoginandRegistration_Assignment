package com.optimum.dao;

import java.sql.SQLException;

import com.optimum.pojo.User;

public interface Blueprint_AccessDatabase {
	//Check methods
	boolean checkAdminCred(User refUser) throws SQLException;
	boolean checkUserCred(User refUser) throws SQLException;
	
	//Implementation methods
	void incrementUserAttempt(User refUser);
	boolean accountLockNotif(User refUser) throws SQLException;
	void lockAccount(String id) throws SQLException;
	void firstLogin(User refUser);
	boolean checkFirstLogin(User refUser) throws SQLException;
	boolean checkPass(String pass,String email) throws SQLException;
	void setNewPassword(String ID, String newPass) throws SQLException;
	String getSecurityQuestion(String email) throws SQLException;
	void setSecurityQuestion(String ques, String emailID) throws SQLException;
	void setSecurityAnswer(String ans, String emailID) throws SQLException;
	boolean getSecurityAnswer(String answer,String email) throws SQLException;
	
	//Admin menu methods
	void returnUserList() throws SQLException;
	 void unlockAccount(String email) throws SQLException;
	 
	 //Registration methods
	 boolean ifNRICExists(String NRIC) throws SQLException;
	 boolean ifEmailExists(String email) throws SQLException;
	 boolean ifMobileNoExists(int num) throws SQLException;
	 void insertIntoTable(String name, String nric, String dob,String email,int mobile,String pass);
	 
}
