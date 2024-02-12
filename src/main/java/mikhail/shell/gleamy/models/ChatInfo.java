package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component 
@Scope("prototype")
@Data
@Entity
@Table(name = "chats")
public class ChatInfo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
	private Message last;
    @OneToMany
    private Map<Long, User> users;
}
