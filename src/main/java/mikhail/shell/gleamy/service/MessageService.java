package mikhail.shell.gleamy.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.api.ActionModel;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public final class MessageService {
	@Autowired
	private final SimpMessagingTemplate simpJms;
	@Autowired
	private final MessagesRepository messagesRepository;

	public Message getMessage(Long msgid) throws EntityNotFoundException
	{
		return messagesRepository.findById(msgid).orElseThrow(EntityNotFoundException::new);
	}
	public List<Message> getChatMessages(Long chatid)
	{
		return messagesRepository.findByChatid(chatid);
	}
	public Message addMessage(Message msg)
	{
		LocalDateTime dt = LocalDateTime.now();
		msg.setDatetime(dt);
		msg = messagesRepository.save(msg);
		simpJms.convertAndSend(
				"/topics/chats/" + msg.getChatid(),
				new ActionModel("NEWMESSAGE",msg)
		);
		return msg;
	}
	public Message editMessage(Message msg)
	{
		if (!messagesRepository.existsById(msg.getMsgid()))
			throw new IllegalArgumentException();
		simpJms.convertAndSend(
				"/topics/chats/" + msg.getChatid(),
				new ActionModel("EDITMESSAGE", msg)
		);
		return messagesRepository.save(msg);
	}
	public Boolean deleteMessage(Long msgid)
	{
		if (!messagesRepository.existsById(msgid))
			throw new IllegalArgumentException();
		simpJms.convertAndSend(
				"/topics/chats/"+msgid,
				new ActionModel("DELETEMESSAGE", msgid)
		);
		return !messagesRepository.existsById(msgid);
	}
}