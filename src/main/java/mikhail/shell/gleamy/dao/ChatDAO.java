package mikhail.shell.gleamy.dao;

import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ChatDAO extends AbstractDAO{
    
    @Autowired
    public ChatDAO(ApplicationContext appContext, 
            JdbcTemplate jdbc)
    {
        super(appContext, jdbc);
    }
    public List<Chat> getAllChats(long userid)
    {
        String sql = "SELECT * FROM `chats` \n" +
                    "INNER JOIN `users_in_chats`\n" +
                    "ON chats.id = users_in_chats.chatid\n" +
                    "WHERE userid = ?;";
        List<Chat> chats = getJdbc().query(sql,
                new Object[]{userid},
                new BeanPropertyRowMapper(Chat.class));
        return chats;
    }
    public Chat getChat(long chatid)
    {
        String sql = "SELECT * FROM `chats` WHERE `id` = ?;";
        List<Chat> chats = getJdbc().query(sql,
                new Object[]{chatid},
                new BeanPropertyRowMapper(Chat.class));
        if (!chats.isEmpty())
            return chats.get(0);
        else
            return null;
    }

    public long add(Chat chat) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
        int addChat = getJdbc().update(
						connection -> {
							String sql = "INSERT INTO `chats`"
								+ "(`title`) VALUES (?);";
							PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, chat.getTitle());
							return ps;
						},
						keyHolder
                        );
		chat.setId(keyHolder.getKey().longValue());
        if (addChat==0)
            return 0;
        String sql = "INSERT INTO `users_in_chats` VALUES ";
        for(Long uid : chat.getUsers().keySet())
        {
            sql += "('"+ chat.getId()+"','"+uid+"') ,";
        }
		sql = sql.substring(0,sql.length()-1) + ";";
        int assignUsers = getJdbc().update(sql);
        if (assignUsers==0)
            return 0;
        
        return keyHolder.getKey().longValue();
    }
	public List<Long> getUserIdsFromChat(long chatid)
	{
		return getJdbc().queryForList("SELECT userid FROM users_in_chats WHERE chatid = " + chatid, Long.class);
	}
	public List<User> getChatMembers(long chatid)
	{
		String sql = "SELECT * FROM `users` INNER JOIN users_in_chats ON `users`.id = `users_in_chats`.`userid` WHERE chatid = ?";
		return getJdbc().query(sql, new Object[]{chatid}, new BeanPropertyRowMapper(User.class));
	} 
}
