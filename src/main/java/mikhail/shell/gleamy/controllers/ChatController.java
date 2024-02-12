package mikhail.shell.gleamy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.Message;

import mikhail.shell.gleamy.dao.ChatDAO;
import mikhail.shell.gleamy.dao.MessageDAO;

import mikhail.shell.gleamy.service.ChatService;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatDAO chDAO;
	private final ChatService chatService;
	private final MessageDAO msgDAO;
    private final ChatsRepo chatsRepo;
    private final MessagesRepo messagesRepo;
    @Autowired
    public ChatController(
            ChatService chatService,
            ChatDAO chDAO,
            MessageDAO msgDAO,
            ChatsRepo chatsRepo,
            MessagesRepo messagesRepo)
    {
		this.chatService = chatService;
        this.chDAO = chDAO;
		this.msgDAO = msgDAO;
        this.chatsRepo = chatsRepo;
        this.messagesRepo = messagesRepo;
    }
    @GetMapping("/users/{userid}")
	@ResponseBody
    public Map<Long,ChatInfo> getAllChats(@PathVariable("userid") long userid)
    {
        List<ChatInfo> chats = chDAO.getAllChats(userid);
		List<Message> msgs = msgDAO.getLastMsgs(userid);
		Map<Long, ChatInfo> chatInfos = new HashMap<Long, ChatInfo>();
		for (ChatInfo chat : chats)
		{
			chatInfos.put(chat.getId(), chat);
		}
		for (Message msg : msgs)
		{
			chatInfos.get(msg.getChatid()).setLast(msg);
		}
		chatInfos = new LinkedHashMap<Long, ChatInfo>(chatInfos);
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
    public Map<String, String> addChat(@RequestBody ChatInfo chat)
    {
		long chatid = chDAO.add(chat);
		chat.setId(chatid);
		Map<String, String> response = new HashMap<>();
		response.put("chatid", chatid + "");
        return response;
    }

    @GetMapping("/{chatid}/members/all")
    public ResponseEntity<List<Long>> getAllMembersByChatId(@PathVariable long chatid)
    {
        if (!chatsRepo.existsById(chatid))
            return ResponseEntity.notFound().build();
        else
        {
            List<Long> ids = null;//messagesRepo.findAllMembersIdsByChatid(chatid);
            return ResponseEntity.ok(ids);
        }
    }
}
