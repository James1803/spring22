package com.qa.intro_project.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.qa.intro_project.data.entity.Post;
import com.qa.intro_project.data.entity.Tag;
import com.qa.intro_project.data.repository.PostRepository;
import com.qa.intro_project.data.repository.TagRepository;

@Profile("dev") // Only runs on the dev profile
@Configuration // Indicates this file contains bean candidates for component scanning
public class DevStartupListener implements ApplicationListener<ApplicationReadyEvent> {
	
	private PostRepository postRepository;
	private TagRepository tagRepository;
	
	@Autowired
	public DevStartupListener(PostRepository postRepository, TagRepository tagRepository) {
		this.postRepository = postRepository;
		this.tagRepository = tagRepository;
	}

	// Waits until the app is truly ready before saving the users
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		List<Tag> tags = tagRepository.saveAll(List.of(
			new Tag("JAVA"),
			new Tag("AWESOME"),
			new Tag("JAVASCRIPT"),
			new Tag("PYTHON")
		));
		
		List<Post> posts = postRepository.saveAll(List.of(
				new Post("Test 1", "Some description", tags.subList(0, 1)),
				new Post("Test 2", "Some other description", tags.subList(1, 2)),
				new Post("Test 3", "Some alternate description", tags.subList(2, tags.size()))
		));

	}

}
