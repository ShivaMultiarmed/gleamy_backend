package mikhail.shell.gleamy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import mikhail.shell.gleamy.dao.MessageDAO;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.service.MessageService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageDAO msgDAO;
	private final MessageService msgService;
    
    public MessageController(MessageDAO msgDAO, MessageService msgService)
    {
        this.msgDAO  = msgDAO;
		this.msgService = msgService;
    }
    
    @GetMapping("/{msgid}")
    @ResponseBody
    public MsgInfo getMsg(@PathVariable("msgid") long id)
    {
        MsgInfo info = msgDAO.getMsg(id);
        return info;
    }
    @GetMapping("/chat/{chatid}")
	@ResponseBody
    public List<MsgInfo> getChatMsgs(@PathVariable("chatid") long id)
    {
        List<MsgInfo> msgs = msgDAO.getChatMsgs(id);
        return msgs;
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Long> addMessage(@RequestBody MsgInfo msg)
    {
		Map<String, Long> response = new HashMap<>();
		long msgid = msgDAO.add(msg);
		msg.setMsgid(msgid);
		response.put("msgid", msgid);
		msgService.notifyChatMembers(msg);
        return response;
    }
    @PatchMapping ("/{msgid}/edit") 
    @ResponseBody
    public Boolean editMessage(@RequestBody MsgInfo msg)
    {
        return msgDAO.edit(msg);
    }
    @DeleteMapping ("/{msgid}/delete")
    public Boolean removeMessage(long id)
    {
        return msgDAO.remove(id);
    }
}
