package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.List;
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
public class Chat implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
	//private Message last;
    @OneToMany
    private List<User> users;
}
