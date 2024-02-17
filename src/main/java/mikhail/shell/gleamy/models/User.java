package mikhail.shell.gleamy.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login, password, email, avatar;
}
