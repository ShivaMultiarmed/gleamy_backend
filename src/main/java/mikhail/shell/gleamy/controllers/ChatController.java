package mikhail.shell.gleamy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;

import mikhail.shell.gleamy.dao.ChatDAO;
import mikhail.shell.gleamy.dao.MessageDAO;

import mikhail.shell.gleamy.service.ChatService;

@RestController
@RequestMapping("/api/v1/chats")
@AllArgsConstructor
public class ChatController {
    @Autowired
	private final ChatService chatService;
    @GetMapping("/users/{userid}")
    public ResponseEntity<List<Chat>> getAllChats(@PathVariable Long userid)
    {
        return ResponseEntity.ok(chatService.getAllChats(userid));
    }
    @GetMapping("/{chatid}")
    public ResponseEntity<Chat> getChat(@PathVariable Long chatid)
    {
        if (chatid == null)
            return ResponseEntity.badRequest().build();
        Chat chat = chatService.getChat(chatid);
        return chat != null ? ResponseEntity.ok(chat) : ResponseEntity.notFound().build();
    }
    @PostMapping("/add")
    public ResponseEntity<Chat> addChat(@RequestBody Chat chat)
    {
        if (chat == null)
            return ResponseEntity.badRequest().build();
        try {
            return ResponseEntity.ok(chatService.addChat(chat));
        }
        catch (EntityExistsException e)
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/{chatid}")
    public ResponseEntity<Chat> editChat(@RequestBody Chat chat)
    {
        if (chat == null)
            return ResponseEntity.badRequest().build();
        try {
            return ResponseEntity.ok(chatService.editChat(chat));
        }
        catch (EntityNotFoundException e)
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{chatid}")
    public ResponseEntity deleteChat(@PathVariable Long chatid)
    {
        if (chatid == null)
            return ResponseEntity.badRequest().build();
        try{
            return chatService.removeChat(chatid) ?
                    ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
        }catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{chatid}/members/all")
    public ResponseEntity<List<User>> getAllChatMembers(@PathVariable Long chatid)
    {
        return null;
    }
}
