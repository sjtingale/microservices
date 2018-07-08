package com.myspring.firstapp.springbootstart.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.myspring.firstapp.springbootstart.classes.Post;
import com.myspring.firstapp.springbootstart.classes.PostRepository;
import com.myspring.firstapp.springbootstart.classes.User;
import com.myspring.firstapp.springbootstart.classes.UserDAOService;
import com.myspring.firstapp.springbootstart.classes.UserRepository;
import com.myspring.firstapp.springbootstart.filteredop.SomeBean;;

@RestController
public class JPAController {
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;

	@GetMapping(path = "/jpa/users")
	public List<User> listUser() {
		return  userRepo.findAll();
	}
	
	@GetMapping(path="/jpa/filter-data")
	public MappingJacksonValue filteredData()
	{
		SomeBean someBean= new SomeBean("user","user123","nickname");
		SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.filterOutAllExcept("username","nickName");
		FilterProvider filters= new SimpleFilterProvider().addFilter("someBeanFilter", filter);
		MappingJacksonValue jacksonValue= new MappingJacksonValue(someBean);
		jacksonValue.setFilters(filters);
		return jacksonValue;
	}

	@GetMapping(path = "/jpa/users/{id}", produces = "application/hal+json")
	public Resource<User> findUser(@PathVariable int id ) {
		Optional<User> user= userRepo.findById(id);
		if(!user.isPresent())
		{
			throw new UserNotFoundException("id:"+id +" does not exist");
		}
		
		Resource<User> resource= new Resource<User>(user.get());
		ControllerLinkBuilder linkTo=linkTo(methodOn(this.getClass()).listUser()); //actual method for which link need to be created
		resource.add(linkTo.withRel("all-users"));//what wrapper to display
		return resource;
	}

	@PostMapping(path = "/jpa/users")
	public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
		User savedUser = userRepo.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	@DeleteMapping(path="/jpa/users/{id}")
	public void delUser(@PathVariable int id)
	{
		userRepo.deleteById(id);
		
	}
	@GetMapping(path="/jpa/hello")
	public String hello(@RequestHeader(name="Accept-Language", required=false) Locale locale)
	{
		return messageSource.getMessage("good.morning.message", null, locale);
	}
	@GetMapping(path = "/jpa/users/{id}/posts", produces = "application/hal+json")
	public List<Post> getPosts(@PathVariable int id ) {
		Optional<User> user= userRepo.findById(id);
		if(!user.isPresent())
		{
			throw new UserNotFoundException("id:"+id +" does not exist");
		}
		
		return user.get().getPosts();
	}

	@PostMapping(path = "/jpa/users/{id}/posts", produces = "application/hal+json")
	public ResponseEntity<Object> posts(@PathVariable int id,@Valid @RequestBody Post post ) {
		Optional<User> user= userRepo.findById(id);
		
		if(!user.isPresent())
		{
			throw new UserNotFoundException("id:"+id +" does not exist");
		}
		post.setUser(user.get());
		postRepo.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();
		
	}
}
