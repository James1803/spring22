package com.qa.intro_project.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qa.intro_project.data.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
}
