package com.optimum.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnector {
	

	public static Connection getCon(){
		Connection con = null;
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			 con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/LoginAuthData","root","root");  
			
			}catch(Exception e){
				System.out.println(e); 
			}
		return con;
		}
	}



