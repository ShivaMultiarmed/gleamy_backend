package mikhail.shell.gleamy.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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
    public long insertUser(User user)
    {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
        jdbc.update( connection -> {
				String query = "INSERT INTO `users` (`login`, `password`, `email`)"
                + "VALUES (?, ?, ?)";
				PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getLogin());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getEmail());
				return ps;
			}, keyHolder
		);
		return keyHolder.getKey().longValue();
    }
	public List<User> search(String query)
	{
		String sql = "SELECT * FROM users WHERE login LIKE '%"+query+"%';";
		List<User> users = jdbc.query(sql, new BeanPropertyRowMapper<>(User.class));
		return users;
	}
}
