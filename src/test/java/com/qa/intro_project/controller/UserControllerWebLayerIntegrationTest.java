package com.qa.intro_project.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.qa.intro_project.controllers.UserController;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.UserDTO;
import com.qa.intro_project.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

// Spin up web layer components, and a application context
// that is aware of the UserController bean
@WebMvcTest(UserController.class)
@ActiveProfiles({ "test" })
public class UserControllerWebLayerIntegrationTest {
	
	@MockBean // equivalent to Mockitos @Mock
	private UserService userService;

	@Autowired
	private UserController userController;
	
	private List<UserDTO> userDTOs;
	
	@BeforeEach
	public void init() {
		userDTOs = List.of(new UserDTO(1, "Bob", "bob@mail.com"), new UserDTO(2, "Sarah", "sarah@mail.com"));
	}
	
	@Test
	public void getAllUsersTest() {
		// arrange
		ResponseEntity<?> expected = ResponseEntity.ok(userDTOs);
		when(userService.getUsers()).thenReturn(userDTOs);
		
		// act
		ResponseEntity<?> actual = userController.getUsers();
		
		// assert
		assertEquals(expected, actual);
		verify(userService).getUsers();
	}
	
	@Test
	public void createUserTest() {
		// arrange
		NewUserDTO newUser = new NewUserDTO();
		newUser.setUsername("Fred");
		newUser.setEmail("fred@mail.com");
		
		UserDTO expectedUser = new UserDTO(1, newUser.getUsername(), newUser.getEmail());
		ResponseEntity<?> expected = ResponseEntity.created(URI.create("http://localhost:8080/user/" + expectedUser.getId()))
												   .body(expectedUser);
		
		when(userService.createUser(newUser)).thenReturn(expectedUser);
		
		// act
		ResponseEntity<?> actual = userController.createUser(newUser);
		
		// assert
		assertEquals(expected, actual);
		verify(userService).createUser(newUser);
	}
}
