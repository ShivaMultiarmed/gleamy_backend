package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Transient
	private Message last;
    @ManyToMany
    @JoinTable(
            name = "users_in_chats",
            joinColumns = @JoinColumn(name="chatid"),
            inverseJoinColumns = @JoinColumn(name="userid")
    )
    private Set<User> users;
}
