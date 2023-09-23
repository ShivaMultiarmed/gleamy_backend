package mikhail.shell.gleamy.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter @Setter
public class MsgInfo implements Serializable {
    private long msgid, chatid, userid;
    private String text;
}
