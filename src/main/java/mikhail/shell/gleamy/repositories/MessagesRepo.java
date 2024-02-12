package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepo extends JpaRepository<Message, Long> {
    /*@Modifying
    @Query("SELECT u.id FROM MsgInfo m INNER JOIN User u ON u.id = m.userid WHERE m.chatid = :chatid GROUP BY u.id ORDER BY m.datetime")
    List<Long> findAllMembersIdsByChatid(@Param("chatid") Long chatid);*/
    List<Message> findByChatid(Long chatid);
}
