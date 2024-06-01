package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
public class Chat implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @CreationTimestamp
    private LocalDateTime datetime;
}
