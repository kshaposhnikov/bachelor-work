package com.shaposhnikov.facerecognizer.grabber;

import com.shaposhnikov.facerecognizer.command.Command;
import com.shaposhnikov.facerecognizer.util.Callable;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.Executors;

/**
 * Created by Kirill on 26.02.2017.
 */
public class WebCamGrabber extends AbstractFrameGrabber {

    private final VideoCapture capture;

    public WebCamGrabber(Command command) {
        super(Executors.newSingleThreadScheduledExecutor(), command);
        this.capture = new VideoCapture();
    }

    @Override
    public void start() {
        if (!capture.isOpened()) {
            capture.open(0);
        }
        //super.start();
    }

    @Override
    public void stop() {
        if (capture.isOpened()) {
            capture.release();
        }
        super.stop();
    }

    public Mat grab() {
        if (!capture.isOpened()) {
            throw new RuntimeException("Video Capture is closed.");
        }

        Mat srcImage = new Mat();
        capture.read(srcImage);
        return srcImage;
    }
}
