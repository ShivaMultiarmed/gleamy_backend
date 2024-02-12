package mikhail.shell.gleamy.dao;

import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;

import mikhail.shell.gleamy.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageDAO extends AbstractDAO {
    @Autowired
    public MessageDAO(ApplicationContext appContext,  
            JdbcTemplate jdbc)
    {
        super(appContext, jdbc);
    }
    public Message getMsg(long id)
    {
        String sql = "SELECT * FROM `messages` WHERE `id` = ?;";
        List<Message> msgs = getJdbc()
                .query(sql,
                        new Object[]{id}, 
                        new BeanPropertyRowMapper(Message.class));
        if (!msgs.isEmpty())
            return msgs.get(0);
        else
            return null;
    }
    public List<Message> getChatMsgs(long chatid)
    {
        String sql = "SELECT * FROM `messages` INNER JOIN `users` ON `users`.id = `userid` WHERE `chatid` = ? ;";
        List<Message> msgs = getJdbc().query(
                                        sql,
                                        new Object[]{chatid},
                                        new BeanPropertyRowMapper(Message.class)
                                    );
        return msgs;
    }
	public List<Message> getLastMsgs(long userid)
	{
		String sql = "SELECT `messages`.`chatid`, `messages`.`userid`, `messages`.text, messages.msgid FROM messages WHERE msgid IN (SELECT MAX(messages.msgid) FROM messages INNER JOIN `users_in_chats` ON `users_in_chats`.`chatid` = `messages`.`chatid`  WHERE `users_in_chats`.`userid` = ? GROUP BY `messages`.`chatid`) ;";
		List<Message> msgs = getJdbc().query(
			sql,
			new Object[]{userid},
			new BeanPropertyRowMapper(Message.class)
		);
		return msgs;
	}
	public Message getLastMsg(long chatid)
	{
		String sql = "SELECT * FROM `messages` WHERE `chatid` = ? ORDER BY `id` DESC LIMIT 0,1;";
		List<Message> msgs = getJdbc().query(
			sql,
			new Object[] {chatid},
			new BeanPropertyRowMapper(Message.class)
		);
		if (msgs.isEmpty())
			return null;
		else	
			return msgs.get(0);
	}
    public long add(Message msg) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		
        getJdbc().update(
				connection -> {
					String sql = "INSERT INTO `messages`"
						+ "(`userid`, `chatid`, `text`, `dateTime`)"
						+ "VALUES(?,?,?,?);";
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1,msg.getUserid());
					ps.setLong(2,msg.getChatid());
					ps.setString(3, msg.getText());
					ps.setObject(4, msg.getDatetime());
					return ps;
				},
				keyHolder
                
        );
		return keyHolder.getKey().longValue();
    }

    public Boolean edit(Message msg) {
        return (getJdbc()
                .update(
                        "UPDATE `messages`"
                        + "SET `text` = ?"
                        + "WHERE id = ?;", 
                        new Object[]{
                                        msg.getText(),
                                        msg.getMsgid()
                                    }
                        ) >0);
    }

    public Boolean remove(long id) {
        return ( getJdbc()
                .update("DELETE FROM `messages` WHERE id  = ?",
                        id)> 0); 
    }
}
