package com.gamix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private Dotenv dotenv;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println(dotenv.get("FRONT_END_BASE_URL"));
        registry.addEndpoint("/ws")
            .setAllowedOrigins(dotenv.get("FRONT_END_BASE_URL"))
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }
}
