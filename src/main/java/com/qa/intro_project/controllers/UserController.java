package com.qa.intro_project.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.intro_project.data.entity.Post;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.PostDTO;
import com.qa.intro_project.dto.UserDTO;
import com.qa.intro_project.service.UserService;

@RestController
@RequestMapping(path = "/user") // accepts requests at localhost:8080/user
public class UserController {
	
	private UserService userService;
	
	@Autowired // Instructs the Spring IoC container to inject the required dependency
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> getUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") int id) {
		UserDTO user = userService.getUser(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping(path = "/{id}/posts")
	public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable(name = "id") int userId) {
		return ResponseEntity.ok(userService.getUserPosts(userId));
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody NewUserDTO user) {
		UserDTO newUser = userService.createUser(user);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "http://localhost:8080/user/" + newUser.getId());

		return new ResponseEntity<>(newUser, headers, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{id}") // could be preferable to create an UpdateUserDTO if user is expanded more
	public ResponseEntity<UserDTO> updateUser(@RequestBody NewUserDTO newUserDTO, @PathVariable(name = "id") int id) {
		return ResponseEntity.ok(userService.updateUser(newUserDTO, id));
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int id) {
		UserDTO deletedUser = userService.getUser(id);
		userService.deleteUser(id);
		return ResponseEntity.ok(deletedUser);
	}
}
