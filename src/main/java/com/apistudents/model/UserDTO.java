package com.apistudents.model;

import java.io.Serializable;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long userId;
	private String name;
	
	public UserDTO(Users user) {
		this.userId = user.getId();
		this.name = user.getName();
	}
	public Long getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
