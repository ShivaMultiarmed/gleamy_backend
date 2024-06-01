package mikhail.shell.gleamy.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepository;
import mikhail.shell.gleamy.repositories.MessagesRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;

import mikhail.shell.gleamy.api.ActionModel;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class ChatService
{
	@Autowired
	private final SimpMessagingTemplate jmsTpl;
	@Autowired
	private final ChatsRepository chatsRepository;
	private final MessagesRepository messageRepo;
	public List<Chat> getAllChats(Long userid)
	{
		List<Chat> chatList = chatsRepository.getUserChats(userid);
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
		chatList.sort((chat1, chat2) -> {
			final Message msg1 = chat1.getLast(), msg2 = chat2.getLast();
			final LocalDateTime dt1 = (msg1 != null) ? msg1.getDatetime() : chat1.getDatetime();
			final LocalDateTime dt2 = (msg2 != null) ? msg2.getDatetime() : chat2.getDatetime();
			return dt2.compareTo(dt1);
		});
	}
	public Chat addChat(Chat chat) throws EntityExistsException
	{
		if(chatsRepository.existsById(chat.getId()))
			throw new EntityExistsException();
		chat = chatsRepository.save(chat);
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
		return chatsRepository.findById(chatid).orElse(null);
	}
	public List<User> getAllChatMembers(Long chatid) throws IllegalArgumentException
	{
		return null;
	}
	public Chat editChat(Chat chat) throws EntityNotFoundException
	{
		if (!chatsRepository.existsById(chat.getId()))
			throw new EntityNotFoundException();
		chat = chatsRepository.save(chat);
		notifyAllMembers("EDITCHAT", chat);
		return chat;
	}
	public Boolean removeChat(Long chatid) throws EntityNotFoundException
	{
		if (!chatsRepository.existsById(chatid))
			throw new EntityNotFoundException();
		chatsRepository.deleteById(chatid);
		return !chatsRepository.existsById(chatid);
	}
}