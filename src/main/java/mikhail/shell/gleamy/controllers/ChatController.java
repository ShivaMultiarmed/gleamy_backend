package mikhail.shell.gleamy.controllers;

import java.util.List;
import mikhail.shell.gleamy.dao.ChatDAO;
import mikhail.shell.gleamy.models.ChatInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatDAO chDAO;
    @Autowired
    public ChatController(ChatDAO chDAO)
    {
        this.chDAO = chDAO;
    }
    @GetMapping("/users/{userid}")
    public List<ChatInfo> getAllChats(@PathVariable("userid") long userid)
    {
        List<ChatInfo> chats = chDAO.getAllChats(userid);
        return chats;
    }
    @GetMapping("/{chatid}")
    @ResponseBody
    public ChatInfo getChat(@PathVariable("chatid") long chatid)
    {
        ChatInfo chat = chDAO.getChat(chatid);
        return chat;
    }
    @PostMapping("/add")
    @ResponseBody
    public Boolean addChat(@RequestBody ChatInfo chat)
    {
        return chDAO.add(chat);
    }
}
