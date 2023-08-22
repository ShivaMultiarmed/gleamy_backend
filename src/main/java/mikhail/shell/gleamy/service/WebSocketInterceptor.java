package mikhail.shell.gleamy.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

@Override
public Message<?> preSend(Message<?> message, MessageChannel channel) {
// Log the incoming message payload
Object payload = message.getPayload();
logger.info("Received message payload: " + payload);

// You can also log other relevant information if needed
System.out.println("Headers: " + message.getHeaders());
System.out.println("Destination: " + channel.toString());

// Allow the message to continue to its destination
return message;
}
}