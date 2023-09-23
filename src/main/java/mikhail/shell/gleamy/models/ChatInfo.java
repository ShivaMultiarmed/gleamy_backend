package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.lang.Comparable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component 
@Scope("prototype")
@Getter @Setter 
public class ChatInfo implements Serializable, Comparable<ChatInfo>{
    private long id;
    private String title;
	private MsgInfo last;
    private Map<Long, User> users;
	@Override 
	public int compareTo(ChatInfo chat)
	{
		return (int)(this.getLast().getMsgid()-chat.getLast().getMsgid());
	}
}
