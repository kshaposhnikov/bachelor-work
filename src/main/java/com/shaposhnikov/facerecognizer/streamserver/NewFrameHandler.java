package com.shaposhnikov.facerecognizer.streamserver;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by Kirill on 29.04.2017.
 */
public class NewFrameHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String camId = message.getPayload();

        if (!CameraToSessionsCache.containsCamera(camId)) {
            CameraToSessionsCache.addNewCamera(camId);
            CameraToSessionsCache.addNewSession(camId, session);
        } else if (!CameraToSessionsCache.containsSessionForCamera(camId, session)) {
            CameraToSessionsCache.addNewSession(camId, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CameraToSessionsCache.removeSession(session);
    }
}
