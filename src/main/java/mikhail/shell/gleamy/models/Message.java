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
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msgid;
    private long chatid, userid;
    private String text;
	private LocalDateTime datetime;
}
