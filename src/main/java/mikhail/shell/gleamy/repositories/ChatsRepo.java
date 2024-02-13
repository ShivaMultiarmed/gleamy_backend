package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatsRepo extends JpaRepository<Chat, Long> {
    List<Chat> getUserChats(Long userid);
}
