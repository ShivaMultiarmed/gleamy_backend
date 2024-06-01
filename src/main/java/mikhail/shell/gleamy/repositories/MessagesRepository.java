package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatid(Long chatid);
    @Query(nativeQuery = true, value = "WITH RankedMessages AS ( SELECT *, ROW_NUMBER() OVER (PARTITION BY chatid ORDER BY datetime DESC) AS rn FROM messages WHERE chatid IN :chatids ) SELECT * FROM RankedMessages WHERE rn = 1;")
    List<Message> getLastChatMessages(@Param("chatids") List<Long> chatids);
}
