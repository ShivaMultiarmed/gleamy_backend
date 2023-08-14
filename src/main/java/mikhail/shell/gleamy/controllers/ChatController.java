package mikhail.shell.gleamy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
	@ResponseBody
    public Map<Long,ChatInfo> getAllChats(@PathVariable("userid") long userid)
    {
        List<ChatInfo> chats = chDAO.getAllChats(userid);
		Map<Long, ChatInfo> chatInfos = new HashMap<Long, ChatInfo>();
		for (ChatInfo chat : chats)
		{
			chatInfos.put(chat.getId(), chat);
		}
        return chatInfos;
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
    public Map<String, String> addChat(@RequestBody ChatInfo chat)
    {
		long chatid = chDAO.add(chat);
		Map<String, String> response = new HashMap<>();
		response.put("chatid", chatid + "");
        return response;
    }
}
