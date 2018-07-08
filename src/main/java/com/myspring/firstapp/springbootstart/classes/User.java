package com.myspring.firstapp.springbootstart.classes;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Validation info")
@Entity
public class User {

	@Size(min = 3, message = "name should be at least 3 characters")
	@ApiModelProperty(notes = "name shtould be at least 3 chars long")
	String name;

	@ApiModelProperty(notes = "Auto increment field.")
	@JsonIgnore
	@Id
	@GeneratedValue
	Integer id;

	@OneToMany(mappedBy="user") //looks for user column of POST table
	List<Post> posts;
	
	protected User() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Past
	@ApiModelProperty(notes = "Birthdate should be a past value.")
	Date date;

	public Date getDate() {
		return date;
	}

	public User(Integer id, String name, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
