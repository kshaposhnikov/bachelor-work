package com.shaposhnikov.facerecognizer.streamserver;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;

/**
 * Created by Kirill on 30.04.2017.
 */
public class IpWebCamHolder {

    public static final IpCamDriver IP_CAM_DRIVER = new IpCamDriver();

    static {
        Webcam.setDriver(IP_CAM_DRIVER);
    }

    public static void registerNewCam(final IpCamDevice device) {
//        IP_CAM_DRIVER.register(device);
//        Webcam.getWebcams().forEach(webcam -> {
//            if (webcam.getDevice() instanceof IpCamDevice) {
//                IpCamDevice currentDevice = (IpCamDevice) webcam.getDevice();
//                if (!device.getURL().equals(currentDevice.getURL())) {
//                    webcam.addWebcamListener(new SFaceWebcamListener());
//                }
//            }
//        });
    }
}
