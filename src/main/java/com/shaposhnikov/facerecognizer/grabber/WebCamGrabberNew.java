package com.shaposhnikov.facerecognizer.grabber;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDevice;
import com.shaposhnikov.facerecognizer.streamserver.SFaceWebcamListener;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

import java.awt.*;

/**
 * Created by Kirill on 01.04.2017.
 */
public class WebCamGrabberNew implements IFrameGrabber {

    private final Webcam capture;

    public WebCamGrabberNew() {
        this.capture = Webcam.getDefault();
        capture.setViewSize(new Dimension(320, 240));
    }

    @Override
    public void start() {
        if (!capture.isOpen()) {
            capture.open(true);
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
