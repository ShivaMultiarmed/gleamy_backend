package mikhail.shell.gleamy.api;

import lombok.Getter;
import java.io.Serializable;
import java.util.Map;

@Getter
public final class ActionModel<T> implements Serializable
{
	private final String action;
	private final T model;
	private final Map<String,String> details;
	public ActionModel(String action, T model, Map<String, String> details)
	{
		this.action = action;
		this.model = model;
		this.details = details;
	}
	public ActionModel(String action, T model)
	{
		this(action, model, null);
	}
}