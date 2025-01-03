package com.ctrledu.ChatService.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Message broker destinations
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for incoming messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // WebSocket endpoint
                .setAllowedOrigins("http://localhost:3000"); // Allow frontend origin
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();  // SockJS fallback
    }
}
