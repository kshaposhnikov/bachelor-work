package com.shaposhnikov.facerecognizer.grabber;

import com.github.sarxos.webcam.ds.ipcam.*;
import com.shaposhnikov.facerecognizer.streamserver.IpWebCamHolder;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

import java.net.MalformedURLException;

/**
 * Created by Kirill on 28.02.2017.
 */
public class IpWebCamGrabber implements IFrameGrabber {

    private final IpCamDevice capture;

    public IpWebCamGrabber(String cameraName, String ipAddress) throws MalformedURLException {
        this.capture = new IpCamDevice(cameraName, ipAddress, IpCamMode.PUSH);

    }

    public void start() {
        if (!capture.isOpen()) {
            IpWebCamHolder.registerNewCam(capture);
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
        if (!capture.isOpen()) {
            throw new RuntimeException("Video Capture is closed.");
        }
        return ImageConverter.bufferedImageToMat(capture.getImage());
    }
}
