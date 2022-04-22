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

import com.qa.intro_project.data.entity.Post;
import com.qa.intro_project.data.entity.User;
import com.qa.intro_project.data.repository.PostRepository;
import com.qa.intro_project.dto.NewPostDTO;
import com.qa.intro_project.dto.PostDTO;
import com.qa.intro_project.dto.UserDTO;

@ExtendWith(MockitoExtension.class)
public class PostServiceUnitTest {

	@Mock // Equivalent to @MockBean in Spring
	private PostRepository postRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks // equivalent to using @Autowired
	private PostService postService;
	
	private List<User> users;
	private List<UserDTO> userDTOs;
	private List<Post> posts;
	private List<PostDTO> postDTOs;
	
	@BeforeEach
	public void init() {
		users = List.of(new User(1, "Bob", "bob@mail.com"), new User(2, "Sarah", "sarah@mail.com"));
		userDTOs = List.of(new UserDTO(1, "Bob", "bob@mail.com"), new UserDTO(2, "Sarah", "sarah@mail.com"));
		
		posts = List.of(new Post(1, "Test 1", "Content 1", users.get(0)), new Post(2, "Test 2", "Content 2", users.get(1)));
		postDTOs = List.of(new PostDTO(1, "Test 1", "Content 1", userDTOs.get(0), posts.get(0).getPostedAt()), new PostDTO(2, "Test 2", "Content 2", userDTOs.get(1), posts.get(1).getPostedAt()));
	}
	
	@Test
	public void getAllTest() {
		// Arrange-Act-Assert
		// Arrange: setup the data and components under test
		when(postRepository.findAll()).thenReturn(posts);
		when(modelMapper.map(posts.get(0), PostDTO.class)).thenReturn(postDTOs.get(0));
		when(modelMapper.map(posts.get(1), PostDTO.class)).thenReturn(postDTOs.get(1));
		
		// Act: performing the action under test
		List<PostDTO> actual = postService.getPosts();
		
		// Assert: validate the action was successful
		assertEquals(postDTOs, actual);
		verify(postRepository).findAll();
		verify(modelMapper).map(posts.get(0), PostDTO.class);
		verify(modelMapper).map(posts.get(1), PostDTO.class);
	}
	
	@Test
	public void getByIdTest() {
		// Arrange
		Post post = posts.get(0);
		PostDTO postDTO = postDTOs.get(0);
		int id = post.getId();
		
		when(postRepository.findById(id)).thenReturn(Optional.of(post));
		when(modelMapper.map(post, PostDTO.class)).thenReturn(postDTO);
		
		// Act
		PostDTO actual = postService.getPost(id);
		
		// Assert
		assertEquals(postDTO, actual);
		verify(postRepository).findById(id);
		verify(modelMapper).map(post, PostDTO.class);
	}
	
	@Test
	public void getByInvalidIdTest() {
		// Arrange
		int id = 328993;
		when(postRepository.findById(id)).thenReturn(Optional.empty());
		
		// Act
		EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			postService.getPost(id);
		});
		
		// Assert
		String expectedMessage = "Post not found with id " + id;
		assertEquals(expectedMessage, exception.getMessage());
		verify(postRepository).findById(id);
	}
	
	@Test
	public void createTest() {
		// Arrange
		UserDTO userDTO = userDTOs.get(0);
		Post post = posts.get(0);
		Post newPost = new Post(post.getTitle(), post.getContent(), post.getUser());
		PostDTO expected = postDTOs.get(0);
		
		NewPostDTO postDTO = new NewPostDTO(post.getTitle(), post.getContent());
		PostDTO createdPostDTO = new PostDTO(post.getId(), post.getTitle(), post.getContent(), userDTO, post.getPostedAt());
		
		when(modelMapper.map(postDTO, Post.class)).thenReturn(newPost);
		when(postRepository.save(newPost)).thenReturn(post);
		when(modelMapper.map(post, PostDTO.class)).thenReturn(createdPostDTO);
		
		// Act
		PostDTO actual = postService.createPost(postDTO);
		
		// Assert
		assertEquals(expected, actual);
		verify(modelMapper).map(postDTO, Post.class);
		verify(postRepository).save(newPost);
		verify(modelMapper).map(post, PostDTO.class);
	}
}
