package com.practice.fullstackbackendspringboot.config;

import com.practice.fullstackbackendspringboot.utils.StringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint(StringUtil.WEBSOCKET_ENDPOINT).setAllowedOriginPatterns("*").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker(StringUtil.USER_ENDPOINT);
        registry.setApplicationDestinationPrefixes(StringUtil.APP_ENDPOINT);
        registry.setUserDestinationPrefix(StringUtil.USER_ENDPOINT);
    }
}

