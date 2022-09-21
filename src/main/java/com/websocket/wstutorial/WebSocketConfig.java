package com.websocket.wstutorial;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");

        /**
         * 此处和下面的EndPoint并无关联，这里设置/ws 前缀， 前端发送消息的时候需要以 /ws开始， 后端在匹配的时候会删除掉前缀。
         * 比如 Client端 发送消息 /ws/message， 后端匹配的是 @MessageMapping("/message")
         */
        registry.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new UserHandshakeHandler());
    }
}
