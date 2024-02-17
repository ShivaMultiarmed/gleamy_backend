package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatsRepo extends JpaRepository<Chat, Long> {
    @Modifying
    @Query(nativeQuery = true,
            value = "SELECT `chats`.* FROM `chats` INNER JOIN `users_in_chats`" +
                    " ON `chats`.`id` = `users_in_chats`.`chatid` WHERE `users_in_chats`.`userid` = :userid ;")
    List<Chat> getUserChats(@Param("userid") Long userid);
}
