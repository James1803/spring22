package com.qa.intro_project.data.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 
@Table(name = "post")
public class Post {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 32, message = "Title must have at least 3 characters, but no more than 32")
	private String title;
	
	@NotNull
	@NotBlank
	private String content;
	
	// LocalDateTime instead of LocalDate for both time and date
	@NotNull
	private LocalDate postedAt;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	protected Post() {
		super();
		this.postedAt = LocalDate.now();
	}

	public Post(String title, String content) {
		super();
		this.title = title;
		this.content = content;
		this.postedAt = LocalDate.now();
	}

	public Post(String title, String content, User user) {
		super();
		this.title = title;
		this.content = content;
		this.user = user;
		this.postedAt = LocalDate.now();
	}
	
	public Post(int id, String title, String content, User user) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.user = user;
		this.postedAt = LocalDate.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDate postedAt) {
		this.postedAt = postedAt;
	}
	
	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", content=" + content + ", postedAt=" + postedAt + ", user="
				+ user + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, id, postedAt, title, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return Objects.equals(content, other.content) && id == other.id && Objects.equals(postedAt, other.postedAt)
				&& Objects.equals(title, other.title) && Objects.equals(user, other.user);
	}
	
}
