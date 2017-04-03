package com.shaposhnikov.facerecognizer.grabber;

import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;
import com.shaposhnikov.facerecognizer.command.Command;
import com.shaposhnikov.facerecognizer.util.Callable;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

import java.net.MalformedURLException;
import java.util.concurrent.Executors;

/**
 * Created by Kirill on 28.02.2017.
 */
public class IpWebCamGrabber extends AbstractFrameGrabber {

    private final IpCamDevice capture;

    public IpWebCamGrabber(Command command, String cameraName, String ipAddress) throws MalformedURLException {
        super(Executors.newSingleThreadScheduledExecutor(), command);
        this.capture = new IpCamDevice(cameraName, ipAddress, IpCamMode.PUSH);
    }

    public void start() {
        capture.open();
        super.start();
    }

    @Override
    public void stop() {
        capture.close();
        super.stop();
    }

    @Override
    public Mat grab() {
        if (!capture.isOpen()) {
            throw new RuntimeException("Video Capture is closed.");
        }
        return ImageConverter.bufferedImageToMat(capture.getImage());
    }
}
