package com.optimum.controller;

import java.sql.SQLException;
import java.util.Scanner;

public class completeController {
	
	private Scanner ss = new Scanner(System.in);
	private LoginLogic ll = new LoginLogic();
	
	public void launchApp() throws SQLException{
		String pattern = "^[1-3]$";
	
	OUTER:
		while(true) {
			System.out.println("\n---------------WELCOME---------------");
			System.out.println("1. Login");
			System.out.println("2. Forget Password");
			System.out.println("3. Exit");
			
			String userChoice = ss.next();
				
			if(userChoice.matches(pattern)) {
				switch(userChoice) {
				case "1":
					ll.login();
					break;
					
				case "2":
					ll.forgetPassword();
					break;
					
				case "3":
					System.out.println("Thank you!");
					break OUTER;
				}
			}else {
				System.out.println("Please input a valid option.");
				System.out.println("");
				launchApp();
			}
		}
	}
}
