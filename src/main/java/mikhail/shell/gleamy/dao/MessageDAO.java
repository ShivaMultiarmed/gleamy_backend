package mikhail.shell.gleamy.dao;

import java.util.List;
import lombok.Getter;
import mikhail.shell.gleamy.models.MsgInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public boolean add(MsgInfo msg) {
        return (getJdbc().update("INSERT INTO `messages`"
                + "(`userid`, `chatid`, `text`)"
                + "VALUES(?,?,?);", 
                new Object[]{
                    msg.getUserid(),
                    msg.getChatid(),
                    msg.getText()
                }
        ) > 0);
    }

    public Boolean edit(MsgInfo msg) {
        return (getJdbc()
                .update(
                        "UPDATE `messages`"
                        + "SET `text` = ?"
                        + "WHERE id = ?;", 
                        new Object[]{
                                        msg.getText(),
                                        msg.getId()
                                    }
                        ) >0);
    }

    public Boolean remove(long id) {
        return ( getJdbc()
                .update("DELETE FROM `messages` WHERE id  = ?",
                        id)> 0); 
    }
}
