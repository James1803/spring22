package com.qa.intro_project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.intro_project.data.entity.Post;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.PostRepository;
import com.qa.intro_project.dto.NewPostDTO;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.PostDTO;
import com.qa.intro_project.dto.UpdatePostDTO;
import com.qa.intro_project.dto.UserDTO;

@Service
public class PostService {

	private PostRepository postRepository;
	private ModelMapper modelMapper;

	@Autowired
	public PostService(PostRepository postRepository, ModelMapper modelMapper) {
		super();
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}

	public List<PostDTO> getPosts() {
		List<Post> posts = postRepository.findAll();
		List<PostDTO> dtos = new ArrayList<>();
		
		for (Post post : posts) {
			dtos.add(this.toDTO(post));
		}
		return dtos;
	}
	
	public List<PostDTO> getPostsByUserId(int id) {
		List<Post> posts = postRepository.findByUserId(id);
		List<PostDTO> dtos = new ArrayList<>();
		
		for (Post post : posts) {
			dtos.add(this.toDTO(post));
		}
		return dtos;
	}
	
	public PostDTO getPost(int id) {
		Optional<Post> post = postRepository.findById(id);
		
		if (post.isPresent()) {
			return this.toDTO(post.get());
		}
		throw new EntityNotFoundException("Post not found with id " + id);
	}
	
	public PostDTO createPost(NewPostDTO post) {
		Post toSave = this.modelMapper.map(post, Post.class);
		Post newPost = postRepository.save(toSave);
		return this.toDTO(newPost);
	}
	
	public PostDTO updatePost(UpdatePostDTO postDTO, int id) {
		if (postRepository.existsById(id)) {
			Post savedPost = postRepository.getById(id);
			savedPost.setTitle(postDTO.getTitle());
			savedPost.setContent(postDTO.getContent());
			return this.toDTO(postRepository.save(savedPost));
		}
		throw new EntityNotFoundException("Post not found with id " + id);
	}
	
	public void deletePost(int id) {
		if (postRepository.existsById(id)) {
			postRepository.deleteById(id);
			return;
		}
		throw new EntityNotFoundException("Post not found with id " + id); 
	}
	
	private PostDTO toDTO(Post post) {		
		return modelMapper.map(post, PostDTO.class);
	}
}
