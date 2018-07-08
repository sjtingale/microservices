package com.myspring.firstapp.springbootstart.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.stereotype.Component;

@Component
public class UserDAOService {

	private static ArrayList<User> users = new ArrayList<>();
	private static int usercounter = 2;
	static {
		users.add(new User(1, "Adam", new Date()));
		users.add(new User(2, "Eve", new Date()));
	}

	public ArrayList<User> findAll() {
		return users;
	}

	public User save(User user) {
		if (user.getId() == null) {
			user.setId(++usercounter);
		}
		users.add(user);
		return user;
	}

	public User findOne(Integer id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	public User deleteById(Integer id)
	{
		Iterator<User> itr = users.iterator();
		while(itr.hasNext())
		{
			User user= itr.next();
			if (user.getId() == id) {
				itr.remove();
				return user;
			}
		}
		return null;
	}

}
