package mikhail.shell.gleamy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebMvcStompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class JmsConfig implements WebSocketMessageBrokerConfigurer{
	
	@Bean
	public ConnectionFactory connectionFactory()
	{
		PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
		connectionFactory.seBrokerURL("tcp://localhost:61616");
		return connectionFactory;
	}
	@Bean
	public JmsTemplate jmsTemplate()
	{
		return new JmsTemplate(connectionFactory());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry)
	{
		registry.setApplicationDestinationPrefixes("/gleamy");
		registry.enableSimpleBroker("/topics");
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry)
	{
		registry.addEndpoint("/websocket").withSockJS();
	}
	
	
}