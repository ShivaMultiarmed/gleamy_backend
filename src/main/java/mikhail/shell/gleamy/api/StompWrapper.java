package mikhail.shell.gleamy.api;

import lombok.Getter;
import java.io.Serializable;
import java.util.Map;

@Getter
public final class StompWrapper implements Serializable
{
	private final String msgType;
	private final Serializable payload;
	private final Map<String,String> details;
	public StompWrapper(String msgType,Serializable payload, Map<String, String> details)
	{
		this.msgType = msgType;
		this.payload = payload;
		this.details = details;
	}
	public StompWrapper(String msgType,Serializable payload)
	{
		this(msgType, payload, null);
	}
}