package com.example.tastyhub.common.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /***
     * 1.클라이언트(sender)가 메시지를 보내면 서버에 메시지 전달
     * 2.Controller의 @MessageMapping에 의해 메시지를 전달 받음
     * 3.Controller의 @SendTo로 특정 topic
     *
     */

//    private final WebSocketInterceptor webSocketInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
//            .withSockJS()// socket 연결 url
            // 주소 : ws://localhost:8788/chat 으로 연결
            .setAllowedOriginPatterns("*")
            .setAllowedOrigins("*");
//            .withSockJS(); //
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 메세지 구독 요청 url -> 메세지를 받을 때 사용 /sub
        registry.setApplicationDestinationPrefixes("/app"); // 메세지 발행 요청 url -> 메세지를 보낼 때 사 /pub
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(webSocketInterceptor);
//    }
}
