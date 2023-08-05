/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mikhail.shell.gleamy.dao;

import java.util.List;
import lombok.Getter;
import mikhail.shell.gleamy.models.ChatInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatDAO extends AbstractDAO{
    
    @Autowired
    public ChatDAO(ApplicationContext appContext, 
            JdbcTemplate jdbc)
    {
        super(appContext, jdbc);
    }
    public List<ChatInfo> getAllChats(long userid)
    {
        String sql = "SELECT * FROM `chats` \n" +
                    "INNER JOIN `users_in_chats`\n" +
                    "ON chats.id = users_in_chats.chatid\n" +
                    "WHERE userid = ?;";
        List<ChatInfo> chats = getJdbc().query(sql,
                new Object[]{userid},
                new BeanPropertyRowMapper(ChatInfo.class));
        return chats;
    }
    public ChatInfo getChat(long chatid)
    {
        String sql = "SELECT * FROM `chats` WHERE `id` = ?;";
        List<ChatInfo> chats = getJdbc().query(sql,
                new Object[]{chatid},
                new BeanPropertyRowMapper(ChatInfo.class));
        if (!chats.isEmpty())
            return chats.get(0);
        else
            return null;
    }

    public Boolean add(ChatInfo chat) {
        int addChat = getJdbc().update(
                        "INSERT INTO `chats`"
                        + "(`title`) VALUES (?);",
                        chat.getTitle());
        if (addChat==0)
            return false;
        String sql = "INSERT INTO `users_in_chats` VALUES ";
        for(Long uid : chat.getUsers())
        {
            sql += "('"+ chat.getId()+"','"+uid+"')";
        }
        int assignUsers = getJdbc().update(sql);
        if (assignUsers==0)
            return false;
        
        return true;
    }
}
