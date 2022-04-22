package com.qa.intro_project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.intro_project.data.entity.Post;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.PostRepository;
import com.qa.intro_project.data.repository.UserRepository;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.PostDTO;
import com.qa.intro_project.dto.UserDTO;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private PostService postService;
	private ModelMapper modelMapper;

	@Autowired
	public UserService(UserRepository userRepository, PostService postService, ModelMapper modelMapper) {
		super();
		this.userRepository = userRepository;
		this.postService = postService;
		this.modelMapper = modelMapper;
	}

	public List<UserDTO> getUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> dtos = new ArrayList<>();
		
		for (User user : users) {
			dtos.add(this.toDTO(user));
		}
		return dtos;
//		return userRepository.findAll()
//							 .stream()
//							 .map(this::toDTO)
//							 .collect(Collectors.toList());
	}
	
	public UserDTO getUser(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if (user.isPresent()) {
			return this.toDTO(user.get());
		}
		throw new EntityNotFoundException("User not found with id " + id);
//		return this.toDTO(user.orElseThrow(() -> new EntityNotFoundException("User not found with id " + id)));
	}
	
	public UserDTO createUser(NewUserDTO user) {
		User toSave = this.modelMapper.map(user, User.class);
		User newUser = userRepository.save(toSave);
		return this.toDTO(newUser);
	}
	
	@Transactional
	// @Transactional wraps this whole method in a transaction
	// - if a RuntimeException is thrown, the transaction is rolled back (i.e., the changes to the database are not made)
	public UserDTO updateUser(NewUserDTO user, int id) {
		// Alternate way of retrieving a user, no optionals involved
		if (userRepository.existsById(id)) {
			User savedUser = userRepository.getById(id);
			savedUser.setEmail(user.getEmail());
			savedUser.setUsername(user.getUsername());
			return this.toDTO(savedUser);
		}
		throw new EntityNotFoundException("User not found with id " + id);
	}
	
	public void deleteUser(int id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return;
		}
		throw new EntityNotFoundException("User not found with id " + id); 
	}
	
	private UserDTO toDTO(User user) {		
		return this.modelMapper.map(user, UserDTO.class);
		
		// ModelMapper will create an instance of UserDTO
		// - it will then assign the values of all fields in `user`, which have the same name
		//   as the fields in `UserDTO.class`, to that new instance of UserDTO
	}

	public List<PostDTO> getUserPosts(int userId) {
		UserDTO user = this.getUser(userId);
		List<PostDTO> posts = postService.getPostsByUserId(userId);
		posts.forEach(post -> post.setUserDTO(user));
		return posts;
	}
	
}
