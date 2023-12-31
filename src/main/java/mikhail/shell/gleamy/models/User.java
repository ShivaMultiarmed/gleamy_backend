package mikhail.shell.gleamy.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter @Setter 
public class User {
    private long id;
    private String login, password;
}
