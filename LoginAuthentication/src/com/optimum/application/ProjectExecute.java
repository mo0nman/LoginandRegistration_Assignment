package com.optimum.application;

import java.sql.SQLException;

import com.optimum.controller.LoginLogic;
import com.optimum.controller.completeController;
public class ProjectExecute {

	public static void main(String[] args) throws SQLException{
		
		completeController refCon = new completeController();
		
		refCon.launchApp();
	}
}
