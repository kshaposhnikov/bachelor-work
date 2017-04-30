package com.shaposhnikov.facerecognizer.streamserver;

import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kirill on 30.04.2017.
 */
public class CameraToSessionsCache {

    private static final Map<String, List<WebSocketSession>> CAMERA_TO_SESSIONS_CACHE = new ConcurrentHashMap<>();

    private CameraToSessionsCache(){}

    public static List<WebSocketSession> getSessions(String camId) {
        if (CAMERA_TO_SESSIONS_CACHE.containsKey(camId)) {
            return CAMERA_TO_SESSIONS_CACHE.get(camId);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public static boolean containsCamera(String cameraId) {
        return CAMERA_TO_SESSIONS_CACHE.containsKey(cameraId);
    }

    public static boolean containsSessionForCamera(String cameraId, WebSocketSession session) {
        if (CAMERA_TO_SESSIONS_CACHE.containsKey(cameraId)) {
            for (WebSocketSession webSocketSession : CAMERA_TO_SESSIONS_CACHE.get(cameraId)) {
                if (session.equals(webSocketSession)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addNewCamera(String cameraId) {
        CAMERA_TO_SESSIONS_CACHE.put(cameraId, new ArrayList<>());
    }

    public static void addNewSession(String cameraId, WebSocketSession session) {
        if (!CAMERA_TO_SESSIONS_CACHE.containsKey(cameraId)) {
            addNewCamera(cameraId);
        }
        CAMERA_TO_SESSIONS_CACHE.get(cameraId).add(session);
    }

    public synchronized static void removeSession(WebSocketSession session) {
        for (List<WebSocketSession> webSocketSessions : CAMERA_TO_SESSIONS_CACHE.values()) {
            webSocketSessions.remove(session);
        }
    }

//    public static boolean containsSessionForAnyCamera(String sessionId, String cameraId) {
//        if (CAMERA_TO_SESSIONS_CACHE.containsKey(sessionId)) {
//            return CAMERA_TO_SESSIONS_CACHE.get(sessionId).contains(cameraId);
//        }
//        return false;
//    }
//
//    public static void addNewSession(String sessionId) {
//        CAMERA_TO_SESSIONS_CACHE.put(sessionId, new ArrayList<>());
//    }
//
//    public static List<String> getCamerasForSession(String sessionId) {
//        if (!CAMERA_TO_SESSIONS_CACHE.containsKey(sessionId)) {
//            return CAMERA_TO_SESSIONS_CACHE.get(sessionId);
//        } else {
//            return Collections.EMPTY_LIST;
//        }
//    }
//
//    public static void addCameraIdForSession(String sessionId, String cameraId) {
//        if (!CAMERA_TO_SESSIONS_CACHE.containsKey(sessionId)) {
//            CAMERA_TO_SESSIONS_CACHE.put(sessionId, Arrays.asList(cameraId));
//        } else {
//            CAMERA_TO_SESSIONS_CACHE.get(sessionId).add(cameraId);
//        }
//    }
//
//    public static void removeCameraFromSession(String sessionId, String cameraId) {
//        if (CAMERA_TO_SESSIONS_CACHE.containsKey(sessionId)) {
//            CAMERA_TO_SESSIONS_CACHE.get(sessionId).remove(cameraId);
//        }
//    }
//
//    public static void removeSession(String sessionId) {
//        CAMERA_TO_SESSIONS_CACHE.remove(sessionId);
//    }
}
