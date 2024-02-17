package mikhail.shell.gleamy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.ChannelRegistration;
import jakarta.jms.ConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebMvcStompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.Exception;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import java.io.File; 
import java.io.IOException;

@Configuration
@EnableWebSocketMessageBroker
public class JmsConfig implements WebSocketMessageBrokerConfigurer{
	
	private final static String URI = "tcp://127.0.0.1:61616";

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry)
	{
		registry.setApplicationDestinationPrefixes("/gleamy");
		registry.enableSimpleBroker("/topics", "/queues");
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry)
	{
		registry.addEndpoint("/websocket").setAllowedOrigins("*");
	}
}