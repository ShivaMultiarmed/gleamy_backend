package mikhail.shell.gleamy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.repositories.MessagesRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.Getter;

import mikhail.shell.gleamy.dao.AbstractDAO;
import mikhail.shell.gleamy.dao.ChatDAO;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.api.StompWrapper;

@Service
@AllArgsConstructor
public final class MessageService {
	@Autowired
	private final SimpMessagingTemplate simpJms;
	@Autowired
	private final MessagesRepo messagesRepo;

	public Message getMessage(Long msgid) throws EntityNotFoundException
	{
		return messagesRepo.findById(msgid).orElseThrow(EntityNotFoundException::new);
	}
	public List<Message> getChatMessages(Long chatid)
	{
		return messagesRepo.findByChatid(chatid);
	}
	public Message addMessage(Message msg)
	{
		LocalDateTime dt = LocalDateTime.now();
		msg.setDatetime(dt);
		msg = messagesRepo.save(msg);
		simpJms.convertAndSend(
				"/topic/chats/" + msg.getChatid(),
				new StompWrapper("NEWMESSAGE",msg)
		);
		return msg;
	}
	public Message editMessage(Message msg)
	{
		if (!messagesRepo.existsById(msg.getMsgid()))
			throw new IllegalArgumentException();
		simpJms.convertAndSend(
				"/topic/chats/" + msg.getChatid(),
				new StompWrapper("EDITMESSAGE", msg)
		);
		return messagesRepo.save(msg);
	}
	public Boolean deleteMessage(Long msgid)
	{
		if (!messagesRepo.existsById(msgid))
			throw new IllegalArgumentException();
		simpJms.convertAndSend(
				"/topic/chats/"+msgid,
				new StompWrapper("DELETEMESSAGE", msgid)
		);
		return !messagesRepo.existsById(msgid);
	}
}