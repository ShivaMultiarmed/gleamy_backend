package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UsersRepo extends JpaRepository<User, Long> {
    List<User> findByLoginContaining(String login);
    boolean existsByLogin(String login);
    User getByLogin(String login);
    User getByEmail(String email);
    boolean existsByEmail(String email);
    //List<User> getAllChatMembers(Long chatid);
    //List<User> getActiveChatMembers(Long chatid);
}
