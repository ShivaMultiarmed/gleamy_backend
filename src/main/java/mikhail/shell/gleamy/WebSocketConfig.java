package mikhail.shell.gleamy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
	{
		registry.addHandler(appWebSocketHandler(), "/gleamy/websocket").setAllowedOrigins("*");
		//registry.addHandler(appWebSocketHandler(), "/gleamy/websocket").setAllowedOrigins("*");
	}
	@Bean
	public WebSocketHandler appWebSocketHandler()
	{
		return new TextWebSocketHandler(){
			@Override 
			protected void handleTextMessage(WebSocketSession session, TextMessage msg)
			{
				logger.info(msg.toString());
				try{
					session.sendMessage(msg);
				}
				catch(Exception ex)
				{
					ex.printStackTrace(System.err);
				}
			}
		};
	}
}