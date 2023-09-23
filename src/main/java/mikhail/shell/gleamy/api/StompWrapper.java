package mikhail.shell.gleamy.api;

import lombok.Getter;
import java.io.Serializable;

@Getter
public final class StompWrapper implements Serializable
{
	private final String msgType;
	private final Serializable payload;
	public StompWrapper(String msgType,Serializable payload)
	{
		this.msgType = msgType;
		this.payload = payload;
	}
}