package mikhail.shell.gleamy.controllers;

import org.springframework.stereotype.Controller;
import java.lang.Exception;
import org.springframework.jms.annotation.JmsListener;
import mikhail.shell.gleamy.models.Message;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

@Controller
public class TestController
{
	@SubscribeMapping("/topic/users/1")
	public void reactToSubscription()
	{
		System.out.println("Hopefully subscribed");
	}
	@JmsListener(destination = "/topic/users/1")
	public void testMessage(Message message)
	{
		try
		{
			System.out.println(message);
		}
		catch(Exception ex)
		{
			System.out.println("Error in handling msg");
			ex.printStackTrace(System.err);
		}
	}
}