package mikhail.shell.gleamy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

import mikhail.shell.gleamy.models.MsgInfo;

@Service
@Getter @Setter
public class MessageService {

	@Autowired
	private JmsTemplate jmsTpl;
	//private SimpMessagingTemplate msgTpl;

	public void notifyChatMembers(MsgInfo msg) {
		String topic = "/topics/chats/" + msg.getChatid();
		msgTpl.convertAndSend(topic, msg);
	}
}