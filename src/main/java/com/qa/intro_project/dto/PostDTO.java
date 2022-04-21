package com.qa.intro_project.dto;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostDTO {
	
	private int id;

	private String title;

	private String content;
	
	private UserDTO userDTO;
	
	private LocalDate postedAt;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public LocalDate getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDate postedAt) {
		this.postedAt = postedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, id, title, userDTO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostDTO other = (PostDTO) obj;
		return Objects.equals(content, other.content) && id == other.id && Objects.equals(title, other.title)
				&& Objects.equals(userDTO, other.userDTO);
	}

	@Override
	public String toString() {
		return "PostDTO [id=" + id + ", title=" + title + ", content=" + content + ", userDTO=" + userDTO
				+ ", postedAt=" + postedAt + "]";
	}
	
}
