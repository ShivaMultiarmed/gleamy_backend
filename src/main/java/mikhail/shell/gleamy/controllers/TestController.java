package mikhail.shell.gleamy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import java.lang.Exception;
import org.springframework.jms.annotation.JmsListener;
import mikhail.shell.gleamy.models.Message;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

@Controller
class TestController
{
	private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	@SubscribeMapping("/topic/users/1")
	void reactToSubscription()
	{
		LOGGER.info("Hopefully subscribed");
	}
	@JmsListener(destination = "/topic/users/1")
	void testMessage(Message message)
	{
		try
		{
			LOGGER.info(message.getText());
		}
		catch(Exception ex)
		{
			LOGGER.error("Error getting a message from /topic/users/1");
			ex.printStackTrace(System.err);
		}
	}
}