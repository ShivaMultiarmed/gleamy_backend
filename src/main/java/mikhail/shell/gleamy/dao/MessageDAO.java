package mikhail.shell.gleamy.dao;

import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;
import lombok.Getter;
import mikhail.shell.gleamy.models.MsgInfo;
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
    public MsgInfo getMsg(long id)
    {
        String sql = "SELECT * FROM `messages` WHERE `id` = ?;";
        List<MsgInfo> msgs = getJdbc()
                .query(sql,
                        new Object[]{id}, 
                        new BeanPropertyRowMapper(MsgInfo.class));
        if (!msgs.isEmpty())
            return msgs.get(0);
        else
            return null;
    }
    public List<MsgInfo> getChatMsgs(long chatid)
    {
        String sql = "SELECT * FROM `messages` WHERE `chatid` = ?;";
        List<MsgInfo> msgs = getJdbc().query(
                                        sql,
                                        new Object[]{chatid},
                                        new BeanPropertyRowMapper(MsgInfo.class)
                                    );
        return msgs;
    }
	public List<MsgInfo> getLastMsgs(long userid) 
	{
		String sql = "SELECT `messages`.`chatid`, `messages`.`userid`, `messages`.text, messages.msgid FROM messages WHERE msgid IN (SELECT MAX(messages.msgid) FROM messages INNER JOIN `users_in_chats` ON `users_in_chats`.`chatid` = `messages`.`chatid`  WHERE `users_in_chats`.`userid` = ? GROUP BY `messages`.`chatid`) ;";
		List<MsgInfo> msgs = getJdbc().query(
			sql,
			new Object[]{userid},
			new BeanPropertyRowMapper(MsgInfo.class)
		);
		return msgs;
	}
	public MsgInfo getLastMsg(long chatid)
	{
		String sql = "SELECT * FROM `messages` WHERE `chatid` = ? ORDER BY `id` DESC LIMIT 0,1;";
		List<MsgInfo> msgs = getJdbc().query(
			sql,
			new Object[] {chatid},
			new BeanPropertyRowMapper(MsgInfo.class)
		);
		if (msgs.isEmpty())
			return null;
		else	
			return msgs.get(0);
	}
    public long add(MsgInfo msg) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		
        getJdbc().update(
				connection -> {
					String sql = "INSERT INTO `messages`"
						+ "(`userid`, `chatid`, `text`)"
						+ "VALUES(?,?,?);";
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1,msg.getUserid());
					ps.setLong(2,msg.getChatid());
					ps.setString(3, msg.getText());
					return ps;
				},
				keyHolder
                
        );
		return keyHolder.getKey().longValue();
    }

    public Boolean edit(MsgInfo msg) {
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
