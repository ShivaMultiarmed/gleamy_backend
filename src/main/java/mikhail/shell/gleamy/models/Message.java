package mikhail.shell.gleamy.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
//@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msgid;
    private long chatid, userid;
    private String text;
    /*@ManyToOne
    @JoinColumn(table = "users",name = "login")
    private String login;*/
	private LocalDateTime datetime;
}
