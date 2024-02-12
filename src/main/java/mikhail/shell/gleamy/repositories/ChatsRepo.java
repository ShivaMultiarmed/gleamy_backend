package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.ChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepo extends JpaRepository<ChatInfo, Long> {
}
