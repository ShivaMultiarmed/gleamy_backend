package mikhail.shell.gleamy.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;

import mikhail.shell.gleamy.api.ActionModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ChatService
{
	@Autowired
	private final SimpMessagingTemplate jmsTpl;
	@Autowired
	private final ChatsRepo chatsRepo;
	private final MessagesRepo messageRepo;
	public List<Chat> getAllChats(Long userid)
	{
		List<Chat> chatList = chatsRepo.getUserChats(userid);
		populateChatsWithLastMessages(chatList);
		sortChats(chatList);
		return chatList;
	}
	private void populateChatsWithLastMessages(List<Chat> chatList)
	{
		List<Long> chatids = chatList.stream().map(Chat::getId).toList();
		List<Message> msgList = messageRepo.getLastChatMessages(chatids);
		msgList.forEach(msg -> {
			Chat chat = chatList.stream().filter(curChat -> msg.getChatid() == curChat.getId()).findAny().get();
			chat.setLast(msg);
		});
	}
	private void sortChats(List<Chat> chatList)
	{
		//List<Chat> notEmptyChats = chatList.stream().filter(chat -> chat.getLast() != null).toList();
		chatList.sort((chat1, chat2) -> {
			Message msg1 = chat1.getLast(), msg2 = chat2.getLast();
			if (msg1 == null && msg2 == null)
				return 0;
			else if (msg1 == null)
				return 1;
			else if (msg2 == null)
				return -1;
			else{
				LocalDateTime dt1 = msg1.getDatetime(), dt2 = msg2.getDatetime();
				return dt1 != null && dt2 != null ? dt2.compareTo(dt1) : 0;
			}
		});
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
				user -> jmsTpl.convertAndSend("/topics/users/"+user.getId()+"/chats", new ActionModel<>(command,chat))
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