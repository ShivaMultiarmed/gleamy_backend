package mikhail.shell.gleamy.service;

import java.util.Map;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

import mikhail.shell.gleamy.dao.AbstractDAO;
import mikhail.shell.gleamy.dao.ChatDAO;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.api.StompWrapper;

@Service
@Getter
public final class MessageService {
	 
	private final ApplicationContext appContext;
	//private final JmsTemplate jmsTpl;
	private final SimpMessagingTemplate simpJms;
	private final Map<String, AbstractDAO> daos;

	@Autowired
	public MessageService(ApplicationContext appContext, SimpMessagingTemplate simpJms)
	{
		this.appContext = appContext;
		//this.jmsTpl = jmsTpl;
		this.simpJms = simpJms;
		daos = new HashMap<>();
		addDAO("chatDAO",appContext.getBean("chatDAO", ChatDAO.class));
	}
	
	public void addDAO(String name, AbstractDAO dao)
	{
		daos.put(name, dao);
	}

	public void notifyChatMembers(MsgInfo msg) {
		ChatDAO chatDAO = (ChatDAO)daos.get("chatDAO");
		for (Long userid : chatDAO.getUserIdsFromChat(msg.getChatid()))
		{
			simpJms.convertAndSend("/topic/users/" + userid, new StompWrapper("RECEIVEDMESSAGE",msg));
		}
			
	}
}