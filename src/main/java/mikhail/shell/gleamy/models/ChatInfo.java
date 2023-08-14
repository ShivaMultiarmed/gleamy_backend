package mikhail.shell.gleamy.models;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component 
@Scope("prototype")
@Getter @Setter 
public class ChatInfo {
    private long id;
    private String title;
    private Map<Long, User> users;
}
