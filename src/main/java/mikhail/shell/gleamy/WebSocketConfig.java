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

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
	{
		registry.addHandler(appWebSocketHandler(), "/websocket").setAllowedOrigins("*");
	}
	@Bean
	public WebSocketHandler appWebSocketHandler()
	{
		return new TextWebSocketHandler(){
			@Override 
			protected void handleTextMessage(WebSocketSession session, TextMessage msg)
			{
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