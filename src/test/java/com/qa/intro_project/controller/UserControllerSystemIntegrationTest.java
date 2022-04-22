package com.qa.intro_project.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.UserRepository;
import com.qa.intro_project.dto.NewUserDTO;
import com.qa.intro_project.dto.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc // configures a bean for sending requests to our API
@ActiveProfiles({ "test" })
@Sql(scripts = { "classpath:schema.sql", "classpath:user-data.sql" }, 
executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerSystemIntegrationTest {

	@Autowired
	private MockMvc mockMvc; // we use this for sending the HTTP requests in our test
	
	@Autowired
	private ObjectMapper objectMapper; // using for serialising and deserialising
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository; // for retrieving test data

	private List<User> savedUsers;
	private List<UserDTO> savedUserDTOs = new ArrayList<>();
	private int nextId;
	private String uri;
	
	@BeforeEach
	public void init() {
		savedUsers = userRepository.findAll(); // get the test data that user-data.sql initialised
		savedUsers.forEach(user -> savedUserDTOs.add(modelMapper.map(user, UserDTO.class))); // map each user to a dto
		nextId = savedUsers.get(savedUsers.size() - 1).getId() + 1;
		uri = "/user";
	}
	
	@Test
	public void getAllUsersTest() throws Exception {
		// create a http request builder
		// - when sending a request with mockmvc, we use the path only ("/user") for example. This
		//   is because the tests are already aware of the servers URL and port
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.request(HttpMethod.GET, uri);
		
		// specify accept header (content type accepted)
		request.accept(MediaType.APPLICATION_JSON);
		
		// create json string of users for the expected response body
		String users = objectMapper.writeValueAsString(savedUserDTOs);
		
		// create result matchers for the expected response
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(users);
		
		// send request and assert results
		mockMvc.perform(request)
			   .andExpectAll(statusMatcher, contentMatcher);
	}
	
	@Test
	public void createUserTest() throws Exception {
		NewUserDTO newUser = new NewUserDTO();
		newUser.setUsername("Fred");
		newUser.setEmail("fred@mail.com");
		UserDTO expectedUser = new UserDTO(nextId, newUser.getUsername(), newUser.getEmail());
		
		// create request builder
		var request = MockMvcRequestBuilders.request(HttpMethod.POST, uri);
		
		// specify the headers
		request.accept(MediaType.APPLICATION_JSON);
		
		// specify the request body
		request.content(objectMapper.writeValueAsString(newUser));
		request.contentType(MediaType.APPLICATION_JSON);
		
		// create result matchers
		String expectedBody = objectMapper.writeValueAsString(expectedUser);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher locationMatcher = MockMvcResultMatchers.header().string("Location", "http://localhost:8080/user/" + expectedUser.getId());
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(expectedBody);
		
		// perform the request
		mockMvc.perform(request)
			   .andExpectAll(statusMatcher, locationMatcher, contentMatcher);
	}
}
