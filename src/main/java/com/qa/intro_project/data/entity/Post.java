package com.qa.intro_project.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tagged_post", // name of the join table
	           joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), // name of the key of the domain which owns the relationship
	           inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")) // inverse of above
	private List<Tag> tags;
	
	protected Post() {
		super();
		this.tags = new ArrayList<>();
	}

	public Post(String title, String content) {
		super();
		this.title = title;
		this.content = content;
		this.tags = new ArrayList<>();
	}
	
	public Post(String title, String content, List<Tag> tags) {
		super();
		this.title = title;
		this.content = content;
		this.tags = tags;
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", content=" + content + "]";
	}

}
