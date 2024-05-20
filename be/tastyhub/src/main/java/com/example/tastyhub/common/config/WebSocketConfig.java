package com.example.tastyhub.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /***
     * 1.클라이언트(sender)가 메시지를 보내면 서버에 메시지 전달
     * 2.Controller의 @MessageMapping에 의해 메시지를 전달 받음
     * 3.Controller의 @SendTo로 특정 topic
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
//            .withSockJS()// socket 연결 url
            // 주소 : ws://localhost:8788/chat 으로 연결
            .setAllowedOriginPatterns("*"); //
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // prefix 정의,구독, 클라이언트에서 보낸 메세지를 받을 prefix
        registry.enableSimpleBroker("/topic"); // 발행, 해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달
    }
}
