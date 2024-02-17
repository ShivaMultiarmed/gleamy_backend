package mikhail.shell.gleamy.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessageController {
	private final MessageService msgService;
    
    @GetMapping("/{msgid}")
    ResponseEntity<Message> getMessage(@PathVariable Long msgid) {
        if (msgid == null)
            return ResponseEntity.badRequest().build();
        try {
            Message msg = msgService.getMessage(msgid);
            return ResponseEntity.ok(msg);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e)
        {
            e.printStackTrace(System.err);
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/fromchat/{chatid}")
    ResponseEntity<List<Message>> getChatMsgs(@PathVariable Long chatid)
    {
        return ResponseEntity.ok(msgService.getChatMessages(chatid));
    }
    @PostMapping("/add")
    ResponseEntity<Message> addMessage(@RequestBody Message msg) {
		msg = msgService.addMessage(msg);
        return ResponseEntity.ok(msg);
    }
    @PatchMapping ("/edit")
    ResponseEntity<Message> editMessage(@RequestBody Message msg) {
        try {
            return ResponseEntity.ok(msgService.editMessage(msg));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping ("/{msgid}")
    ResponseEntity removeMessage(@PathVariable Long msgid)
    {
        try {
            return msgService.deleteMessage(msgid) ?
                    ResponseEntity.ok().build() : ResponseEntity.internalServerError().body("Error while deleting a message");
        } catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body("No such message.");
        }
    }
}
