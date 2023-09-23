package mikhail.shell.gleamy.service;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.User;

import mikhail.shell.gleamy.api.StompWrapper;

@Service
public final class ChatService
{
	private final ApplicationContext appContext;
	private final JmsTemplate jmsTpl;
	@Autowired
	public ChatService(ApplicationContext appContext, JmsTemplate jmsTpl)
	{
		this.appContext = appContext;
		this.jmsTpl = jmsTpl;
	}
	public void notifyAllMembers(ChatInfo chat)
	{
		long userid;
		for (User user : chat.getUsers().values())
		{
			userid = user.getId();
			jmsTpl.convertAndSend("/topic/users/"+userid, new StompWrapper("NEWCHAT",chat));
		}
	}
}