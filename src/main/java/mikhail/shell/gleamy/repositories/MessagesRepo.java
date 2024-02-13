package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepo extends JpaRepository<Message, Long> {
    List<Message> findByChatid(Long chatid);
}
