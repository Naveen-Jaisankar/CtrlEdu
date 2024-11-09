package jobizzz.ChatService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enables the in-memory message broker
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages bound for @MessageMapping
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {

                    public boolean beforeHandshake(
                            javax.servlet.http.HttpServletRequest request,
                            javax.servlet.http.HttpServletResponse response,
                            org.springframework.web.socket.WebSocketHandler wsHandler,
                            Map<String, Object> attributes) throws Exception {
                        System.out.println("WebSocket handshake attempt from: " + request.getRemoteAddr());
                        return super.beforeHandshake((ServerHttpRequest) request, (ServerHttpResponse) response, wsHandler, attributes);
                    }
                });
    }
}