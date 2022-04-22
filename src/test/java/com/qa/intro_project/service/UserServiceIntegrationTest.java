package com.qa.intro_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.UserRepository;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.UserDTO;

@SpringBootTest // launch the application context and creates the beans (essentially starts out app with a test configuration)
@Sql(scripts = { "classpath:schema.sql", "classpath:user-data.sql" }, 
	 executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles({ "test" }) // use the application-test.properties file
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository; // required to get the test data out of the database
	
	@Autowired
	private ModelMapper modelMapper;
	
	private List<User> savedUsers;
	private List<UserDTO> savedUserDTOs = new ArrayList<>();
	private int nextId;
	
	@BeforeEach
	public void init() {
		savedUsers = userRepository.findAll(); // get the test data that user-data.sql initialised
		savedUsers.forEach(user -> savedUserDTOs.add(modelMapper.map(user, UserDTO.class))); // map each user to a dto
		// for is equivalent to line above
//		for (User user : savedUsers) {
//			savedUserDTOs.add(modelMapper.map(user, UserDTO.class));
//		}
		
		nextId = savedUsers.get(savedUsers.size() - 1).getId() + 1;
	}
	
	@Test
	public void getAllUsersTest() {
		assertEquals(savedUserDTOs, userService.getUsers());
	}
	
	@Test
	public void getUserByIdTest() {
		UserDTO expected = savedUserDTOs.get(0);
		UserDTO actual = userService.getUser(expected.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	public void createUserTest() {
		NewUserDTO user = new NewUserDTO();
		user.setUsername("Eli");
		user.setEmail("eli@mail.com");
		
		UserDTO expected = new UserDTO(nextId, user.getUsername(), user.getEmail());
		UserDTO actual = userService.createUser(user);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void updateUserTest() {
		int id = savedUsers.get(0).getId();
		NewUserDTO user = new NewUserDTO();
		user.setUsername("Eli");
		user.setEmail("eli@mail.com");
		
		UserDTO expected = new UserDTO(id, user.getUsername(), user.getEmail());
		UserDTO actual = userService.updateUser(user, id);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void deleteUserTest() {
		int id = savedUsers.get(0).getId();
		userService.deleteUser(id);
		assertEquals(Optional.empty(), userRepository.findById(id));
	}
}
