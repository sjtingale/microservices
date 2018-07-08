package com.myspring.firstapp.springbootstart.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.myspring.firstapp.springbootstart.classes.User;
import com.myspring.firstapp.springbootstart.classes.UserDAOService;
import com.myspring.firstapp.springbootstart.filteredop.SomeBean;;

@RestController
public class MainController {
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserDAOService service;

	@GetMapping(path = "/users")
	public ArrayList<User> listUser() {
		return service.findAll();
	}
	
	@GetMapping(path="/filter-data")
	public MappingJacksonValue filteredData()
	{
		SomeBean someBean= new SomeBean("user","user123","nickname");
		SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.filterOutAllExcept("username","nickName");
		FilterProvider filters= new SimpleFilterProvider().addFilter("someBeanFilter", filter);
		MappingJacksonValue jacksonValue= new MappingJacksonValue(someBean);
		jacksonValue.setFilters(filters);
		return jacksonValue;
	}

	@GetMapping(path = "/users/{id}", produces = "application/hal+json")
	public Resource<User> findUser(@PathVariable int id ) {
		User user= service.findOne(id);
		if(user==null)
		{
			throw new UserNotFoundException("id:"+id +" does not exist");
		}
		
		Resource<User> resource= new Resource<User>(user);
		ControllerLinkBuilder linkTo=linkTo(methodOn(this.getClass()).listUser()); //actual method for which link need to be created
		resource.add(linkTo.withRel("all-users"));//what wrapper to display
		return resource;
	}

	@PostMapping(path = "/users")
	public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	@DeleteMapping(path="/users/{id}")
	public void delUser(@PathVariable int id)
	{
		User user= service.deleteById(id);
		if(user == null)
			throw new UserNotFoundException("id does not exist");
		
	}
	@GetMapping(path="/hello")
	public String hello(@RequestHeader(name="Accept-Language", required=false) Locale locale)
	{
		return messageSource.getMessage("good.morning.message", null, locale);
	}

}
