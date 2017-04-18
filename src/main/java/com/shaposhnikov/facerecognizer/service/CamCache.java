package com.shaposhnikov.facerecognizer.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kirill on 28.03.2017.
 */
public class CamCache {

    public static final ConcurrentHashMap<String, RecognizeContext> CONTEXT_CACHE = new ConcurrentHashMap<>();
}
