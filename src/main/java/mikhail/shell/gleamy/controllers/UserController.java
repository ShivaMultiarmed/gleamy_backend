package mikhail.shell.gleamy.controllers;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mikhail.shell.gleamy.models.User; 
import mikhail.shell.gleamy.dao.UserDAO;


@RestController
@RequestMapping("/users")
public class UserController
{
	private final UserDAO uDAO;
	@Autowired
	public UserController(UserDAO uDAO)
	{
		this.uDAO = uDAO;
	}
	
	@GetMapping("/search/{login}")
	@ResponseBody
	public Map<Long, User> searchForUsers(@PathVariable("login") String query)
	{
		List<User> uList = uDAO.search(query);
		Map<Long, User> users = new HashMap<>();
		for (User user : uList)
			users.put(user.getId(), user);
		return users;
	}
}