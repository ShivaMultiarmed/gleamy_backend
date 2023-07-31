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
public class UserDAO {
    private ApplicationContext appContext;
    private JdbcTemplate jdbc;
    @Autowired
    public UserDAO(ApplicationContext appContext, 
            JdbcTemplate jdbc)
    {
        this.appContext = appContext;
        this.jdbc = jdbc;
    }
    public User getUser(String login) 
    {
        List<User> users = jdbc.query(
                "SELECT `id`, `login`,`password` FROM `users`"
                        + "WHERE `login` = ?", 
                new String[] {login},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty())
            return users.get(0);
        else
            return null;
    }
}
