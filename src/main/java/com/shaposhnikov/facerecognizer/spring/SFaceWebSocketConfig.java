package com.shaposhnikov.facerecognizer.spring;

import com.shaposhnikov.facerecognizer.streamserver.NewFrameHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

/**
 * Created by Kirill on 29.04.2017.
 */

@Configuration
@EnableWebSocket
public class SFaceWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new PerConnectionWebSocketHandler(NewFrameHandler.class), "/video/stream").withSockJS();
    }
}
