package com.shaposhnikov.facerecognizer.util;

/**
 * Created by Kirill on 28.03.2017.
 */
public interface Callable<V, T> {

    V call(T args);
}
