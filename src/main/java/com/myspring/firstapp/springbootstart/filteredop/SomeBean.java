package com.myspring.firstapp.springbootstart.filteredop;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("someBeanFilter")
public class SomeBean {

	String username,pwd,nickName;

	public SomeBean(String username, String pwd, String nickName) {
		super();
		this.username = username;
		this.pwd = pwd;
		this.nickName = nickName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
