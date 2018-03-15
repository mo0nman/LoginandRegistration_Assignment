package com.optimum.pojo;

public class User {
	private String login;
	private String password;
	
	public User(String userLog, String userPass) {
		this.login = userLog;
		this.password = userPass;
	}

	public String getLogin() {
		return login;
	}

	public void setName(String name) {
		this.login = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
