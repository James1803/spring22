package com.qa.intro_project.data.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotBlank
	@Size(min = 2, max = 16, message = "Username must have at least 2 characters, but no more than 16")
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	// @OneToMany signifies a one to many relationship between user and posts where User is the 
	// parent of the relationship. Post owns the relationship as it stores the id of the user
	// - mappedBy signifies the name of the field in Post.class which owns the relationship
	// - targetEntity specifies the class that is being mapped
	@OneToMany(mappedBy = "user", targetEntity = Post.class, fetch = FetchType.LAZY)
	private List<Post> posts;
		
	protected User() {
		super();
		this.posts = new ArrayList<>();
	}
	
	public User(String username, String email) {
		super();
		this.username = username;
		this.email = email;
		this.posts = new ArrayList<>();
	}

	public User(int id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.posts = new ArrayList<>();
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

	public List<Post> getPosts() {
		return posts;
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, posts, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && id == other.id && Objects.equals(posts, other.posts)
				&& Objects.equals(username, other.username);
	}
	
}
