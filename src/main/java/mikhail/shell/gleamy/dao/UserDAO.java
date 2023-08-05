package mikhail.shell.gleamy.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import mikhail.shell.gleamy.models.User;

@Component 
@Scope("singleton")
public class UserDAO extends AbstractDAO {
    @Autowired
    public UserDAO(ApplicationContext appContext, 
            JdbcTemplate jdbc)
    {
        super(appContext, jdbc);
    }
    public User getUser(String key, String by) 
    {
        String sql;
        if (by.equals("login"))
        {
            sql = "SELECT `id`, `login`,`password`, `email` FROM `users`"
                        + "WHERE `login` = ?";
        }
        else 
        {
            sql  = "SELECT `id`, `login`,`password`, `email` FROM `users`"
                        + "WHERE `email` = ?";
        }
        List<User> users = jdbc.query(sql,
                new String[] {key},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty())
            return users.get(0);
        else
            return null;
    }
    public boolean insertUser(User user)
    {
        return jdbc.update("INSERT INTO `users` (`login`, `password`, `email`)"
                + "VALUES (?, ?, ?)", 
                new String [] {user.getLogin(), 
                user.getPassword(), 
                user.getEmail()}) == 1; 
    }
}
