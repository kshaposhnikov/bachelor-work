package com.shaposhnikov.facerecognizer.command;

/**
 * Created by Kirill on 13.03.2017.
 */
public interface Command<V> {

    V doWork();
}
