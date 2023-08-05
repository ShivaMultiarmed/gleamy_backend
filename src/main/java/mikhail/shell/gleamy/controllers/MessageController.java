package mikhail.shell.gleamy.controllers;

import java.util.List;
import mikhail.shell.gleamy.dao.MessageDAO;
import mikhail.shell.gleamy.models.MsgInfo;
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
    
    public MessageController(MessageDAO msgDAO)
    {
        this.msgDAO  = msgDAO;
    }
    
    @GetMapping("/{msgid}")
    @ResponseBody
    public MsgInfo getMsg(@PathVariable("msgid") long id)
    {
        MsgInfo info = msgDAO.getMsg(id);
        return info;
    }
    @GetMapping("/chat/{chatid}")
    public List<MsgInfo> getChatMsgs(@PathVariable("chatid") long id)
    {
        List<MsgInfo> msgs = msgDAO.getChatMsgs(id);
        return msgs;
    }
    @PostMapping("/add")
    @ResponseBody
    public Boolean addMessage(@RequestBody MsgInfo msg )
    {
        return msgDAO.add(msg);
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
