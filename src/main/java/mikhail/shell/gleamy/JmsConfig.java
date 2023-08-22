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
import mikhail.shell.gleamy.service.WebSocketInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class JmsConfig implements WebSocketMessageBrokerConfigurer{
	
	private final String uri = "tcp://127.0.0.1:61616";
	
	/*@Bean
	public BrokerService brokerService()
	{
		BrokerService bs = new BrokerService();
		bs.setBrokerName("webBroker");
		bs.setPersistent(true);
		bs.setDataDirectory("target/activemq-data");
		TransportConnector connector = new TransportConnector();
		try{
			connector.setUri(new URI("tcp://0.0.0.0:61616"));
			bs.addConnector(connector);
		}
		catch(URISyntaxException ex)
		{
			ex.printStackTrace(System.err);
		}
		catch(Exception ex)
		{
			ex.printStackTrace(System.err);
		}
		
		
		return bs;
	}*/
	@Bean
	public BrokerService brokerService() {
		BrokerService bs = new BrokerService();
		bs.setBrokerName("webBroker");
		bs.setPersistent(true);

		KahaDBPersistenceAdapter kahaDBPersistenceAdapter = new KahaDBPersistenceAdapter();
		kahaDBPersistenceAdapter.setDirectory(new File("target/kahadb-data"));
		try{
			bs.setPersistenceAdapter(kahaDBPersistenceAdapter);
		}
		catch (IOException ex) {
			ex.printStackTrace(System.err);
		}

		

		TransportConnector connector = new TransportConnector();
		try {
			connector.setUri(new URI(uri));
			bs.addConnector(connector);
		} catch (URISyntaxException ex) {
			ex.printStackTrace(System.err);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return bs;
	} 
	
	@Bean
	public ConnectionFactory connectionFactory()
	{
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(uri);
		return connectionFactory;
	}
	@Bean
	public JmsTemplate jmsTemplate()
	{
		JmsTemplate jms = new JmsTemplate();
		jms.setConnectionFactory(connectionFactory());
		return jms;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry)
	{
		registry.setApplicationDestinationPrefixes("/gleamy");
		registry.enableSimpleBroker("topic", "queue");
		//registry.configureBrokerChannel().brokerService(brokerService());
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry)
	{
		registry.addEndpoint("/websocket");
	}
	@Override 
	public void configureClientInboundChannel(ChannelRegistration registration)
	{
		registration.interceptors(new WebSocketInterceptor());
	}
	
}