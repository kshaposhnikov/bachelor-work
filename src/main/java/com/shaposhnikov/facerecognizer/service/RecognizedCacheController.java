package com.shaposhnikov.facerecognizer.service;

import com.shaposhnikov.facerecognizer.service.response.FaceResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Kirill on 23.04.2017.
 */
public class RecognizedCacheController {

    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<FaceResponse>> FACE_CACHE = new ConcurrentHashMap<>();
    private static final Object lock = new Object();

    public static FaceResponse get(String cameraId) {
        if (FACE_CACHE.containsKey(cameraId)) {
            return FACE_CACHE.get(cameraId).poll();
        }

        return null;
    }

    public static boolean containsFaceForCamera(String cameraId, String humanId) {
        if (FACE_CACHE.containsKey(cameraId)) {
            for (FaceResponse faceResponse : FACE_CACHE.get(cameraId)) {
                if (humanId.equals(faceResponse.getHuman().getHumanId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void add(String cameraId, FaceResponse response) {
        synchronized (lock) {
            if (!FACE_CACHE.containsKey(cameraId)) {
                FACE_CACHE.put(cameraId, new ConcurrentLinkedQueue<>());
            } else {
                if (FACE_CACHE.get(cameraId).size() > 20) {
                    FACE_CACHE.get(cameraId).clear();
                }
                FACE_CACHE.get(cameraId).add(response);
            }
        }
    }
}
