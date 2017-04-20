package com.shaposhnikov.facerecognizer.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kirill on 28.03.2017.
 */
public class ContextCacheController {

    private static final ConcurrentHashMap<String, RecognizeContext> CONTEXT_CACHE = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    public static RecognizeContext get(String cameraId) {
        if (!CONTEXT_CACHE.containsKey(cameraId)) {
            return null;
        } else {
            return CONTEXT_CACHE.get(cameraId);
        }
    }

    public static void put(String camId, RecognizeContext context) {
        synchronized (LOCK) {
            if (!CONTEXT_CACHE.containsKey(camId)) {
                CONTEXT_CACHE.put(camId, context);
            }
        }
    }

    public static boolean containsCam(String cameraId) {
        return CONTEXT_CACHE.containsKey(cameraId);
    }

    public static void clearCache() {
        CONTEXT_CACHE.clear();
    }
}
