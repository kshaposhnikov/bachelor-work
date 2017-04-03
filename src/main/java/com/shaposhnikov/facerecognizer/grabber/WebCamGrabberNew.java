package com.shaposhnikov.facerecognizer.grabber;

import com.github.sarxos.webcam.Webcam;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

/**
 * Created by Kirill on 01.04.2017.
 */
public class WebCamGrabberNew implements IFrameGrabber {

    private final Webcam capture;

    public WebCamGrabberNew() {
        this.capture = Webcam.getDefault();
    }

    @Override
    public void start() {
        if (!capture.isOpen()) {
            capture.open();
        }
    }

    @Override
    public void stop() {
        if (capture.isOpen()) {
            capture.close();
        }
    }

    @Override
    public Mat grab() {
        return ImageConverter.bufferedImageToMat(capture.getImage());
    }
}
