package mikhail.shell.gleamy.controllers;

import java.util.List;
import mikhail.shell.gleamy.dao.UserDAO;
import mikhail.shell.gleamy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private UserDAO uDAO;
    
    @Autowired
    public AuthController(UserDAO uDAO)
    {
        this.uDAO = uDAO;
    }
    
    @GetMapping("/login") 
    public String login(@RequestParam("login")String login,
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
        return code;
    }
    @GetMapping("/signup")
    public String signup (@RequestParam("login")String login,
            @RequestParam("password") String password,
            @RequestParam("login") String email)
    {
        String code;
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
            uDAO.insertUser(user);
            code = "OK";
        }
        return code;
    }
}
