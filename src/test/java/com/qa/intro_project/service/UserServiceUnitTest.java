package com.qa.intro_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.UserRepository;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.UserDTO;


// Not using @SpringBootTest because we do not need the whole application context
// 
// Using JUnit 5 (jupiter) @ExtendWith annotation to specify the test runner
// - MockitoExtension.class configures JUnit 5 to use Mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	@Mock // Equivalent to @MockBean in Spring
	private UserRepository userRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks // equivalent to using @Autowired
	private UserService userService;
	
	private List<User> users;
	private List<UserDTO> userDTOs;
	
	@BeforeEach
	public void init() {
		users = List.of(new User(1, "Bob", "bob@mail.com"), new User(2, "Sarah", "sarah@mail.com"));
		userDTOs = List.of(new UserDTO(1, "Bob", "bob@mail.com"), new UserDTO(2, "Sarah", "sarah@mail.com"));
	}
	
	@Test
	public void getAllTest() {
		// Arrange-Act-Assert
		// Arrange: setup the data and components under test
		when(userRepository.findAll()).thenReturn(users);
		when(modelMapper.map(users.get(0), UserDTO.class)).thenReturn(userDTOs.get(0));
		when(modelMapper.map(users.get(1), UserDTO.class)).thenReturn(userDTOs.get(1));
		
		// Act: performing the action under test
		List<UserDTO> actual = userService.getUsers();
		
		// Assert: validate the action was successful
		assertEquals(userDTOs, actual);
		verify(userRepository).findAll();
		verify(modelMapper).map(users.get(0), UserDTO.class);
		verify(modelMapper).map(users.get(1), UserDTO.class);
	}
	
	@Test
	public void getByIdTest() {
		// Arrange
		User user = users.get(0);
		UserDTO userDTO = userDTOs.get(0);
		int id = user.getId();
		
		when(userRepository.findById(id)).thenReturn(Optional.of(user));
		when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
		
		// Act
		UserDTO actual = userService.getUser(id);
		
		// Assert
		assertEquals(userDTO, actual);
		verify(userRepository).findById(id);
		verify(modelMapper).map(user, UserDTO.class);
	}
	
	@Test
	public void getByInvalidIdTest() {
		// Arrange
		int id = 328993;
		when(userRepository.findById(id)).thenReturn(Optional.empty());
		
		// Act
		EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.getUser(id);
		});
		
		// Assert
		String expectedMessage = "User not found with id " + id;
		assertEquals(expectedMessage, exception.getMessage());
		verify(userRepository).findById(id);
	}
	
	@Test
	public void createTest() {
		// Arrange
		User user = users.get(0);
		
		NewUserDTO userDTO = new NewUserDTO();
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		
		UserDTO newUser = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
		
		when(modelMapper.map(userDTO, User.class)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
		when(modelMapper.map(user, UserDTO.class)).thenReturn(newUser);
		
		// Act
		UserDTO actual = userService.createUser(userDTO);
		
		// Assert
		assertEquals(newUser, actual);
		verify(modelMapper).map(userDTO, User.class);
		verify(userRepository).save(user);
		verify(modelMapper).map(user, UserDTO.class);
	}
}
