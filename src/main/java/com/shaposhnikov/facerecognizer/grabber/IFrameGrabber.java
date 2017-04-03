package com.shaposhnikov.facerecognizer.grabber;

import org.opencv.core.Mat;

/**
 * Created by Kirill on 26.02.2017.
 */
public interface IFrameGrabber {

    void start();

    void stop();

    Mat grab();
}
