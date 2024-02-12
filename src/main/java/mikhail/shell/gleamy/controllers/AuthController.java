package mikhail.shell.gleamy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import mikhail.shell.gleamy.dao.UserDAO;
import mikhail.shell.gleamy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private UserDAO uDAO;
    
    @Autowired
    public AuthController(UserDAO uDAO)
    {
        this.uDAO = uDAO;
    }
    
    @PostMapping("/login")
    @ResponseBody
    public Map<String,String> login(@RequestParam("login")String login,
            @RequestParam("password") String password)
    {
        String code;
        User user = uDAO.getUser(login, "login");
        if (user == null)
            code = "NOTFOUND";
        else if (!user.getPassword().equals(password))
            code = "PASSINCORRECT";
        else
            code =  "OK";

		Map<String, String> response = new HashMap<String, String>();
		response.put("code", code);
		if (code.equals("OK"))
			response.put("userid", user.getId()+"");
        return response;
    }
    @PostMapping("/signup")
	@ResponseBody
    public Map<String, String> signup (@RequestParam("login")String login,
            @RequestParam("password") String password,
            @RequestParam("login") String email)
    {
        String code;
		
		Map<String, String> response = new HashMap<>();
        if (uDAO.getUser(login, "login") != null)
        {
            code = "NICKEXISTS";
        }
        else if (uDAO.getUser(email, "email") != null)
        {
            code = "EMAILEXISTS";
        }
        else
        {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            long userid = uDAO.insertUser(user);
			response.put("userid", ""+userid);
            code = "OK";
        }
		
		response.put("code", code);
			
        return response;
    }
	@GetMapping("/test")
	@ResponseBody
	public User test()
	{
		User u = new User();
		u.setEmail("charlie@gmail.com");
		u.setLogin("charlie123");
		u.setPassword("qwerty");
		return u;
	}
}
