package com.qa.intro_project.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {

	private int id;
	
	@NotNull
	@NotBlank
	@Size(min = 2, max = 16, message = "Username must have at least 2 characters, but no more than 16")
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	public UserDTO() {
		super();
	}

	public UserDTO(int id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(email, other.email) && id == other.id && Objects.equals(username, other.username);
	}
	
}
