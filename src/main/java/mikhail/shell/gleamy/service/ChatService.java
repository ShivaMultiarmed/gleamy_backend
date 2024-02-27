package mikhail.shell.gleamy.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;

import mikhail.shell.gleamy.api.StompWrapper;

import java.util.List;

@Service
@AllArgsConstructor
public final class ChatService
{
	@Autowired
	private final SimpMessagingTemplate jmsTpl;
	@Autowired
	private final ChatsRepo chatsRepo;
	public List<Chat> getAllChats(Long userid)
	{
		return chatsRepo.getUserChats(userid);
	}
	public Chat addChat(Chat chat) throws EntityExistsException
	{
		if(chatsRepo.existsById(chat.getId()))
			throw new EntityExistsException();
		chat = chatsRepo.save(chat);
		notifyAllMembers("NEWCHAT",chat);
		return chat;
	}
	private void notifyAllMembers(String command, Chat chat)
	{
		chat.getUsers().forEach(
				user -> jmsTpl.convertAndSend("/topics/users/"+user.getId()+"/chats", new StompWrapper(command,chat))
		);
	}
	public Chat getChat(Long chatid)
	{
		return chatsRepo.findById(chatid).orElse(null);
	}
	public List<User> getAllChatMembers(Long chatid) throws IllegalArgumentException
	{
		return null;
	}
	public Chat editChat(Chat chat) throws EntityNotFoundException
	{
		if (!chatsRepo.existsById(chat.getId()))
			throw new EntityNotFoundException();
		chat = chatsRepo.save(chat);
		notifyAllMembers("EDITCHAT", chat);
		return chat;
	}
	public Boolean removeChat(Long chatid) throws EntityNotFoundException
	{
		if (!chatsRepo.existsById(chatid))
			throw new EntityNotFoundException();
		chatsRepo.deleteById(chatid);
		return !chatsRepo.existsById(chatid);
	}
}