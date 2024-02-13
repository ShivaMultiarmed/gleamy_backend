package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepo extends JpaRepository<User, Long> {
    List<User> findByLoginContaining(String login);
    boolean existsByLogin(String login);
    User getByLogin(String login);
    //List<User> getAllChatMembers(Long chatid);
    //List<User> getActiveChatMembers(Long chatid);
}
