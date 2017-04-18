package com.shaposhnikov.facerecognizer.service.response;

/**
 * Created by Kirill on 19.04.2017.
 */
public class CameraResponse {

    private final String cameraId;
    private final String cameraName;
    private final String cameraDescription;

    public CameraResponse(String cameraId, String cameraName, String cameraDescription) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.cameraDescription = cameraDescription;
    }

    public String getCameraId() {
        return cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getCameraDescription() {
        return cameraDescription;
    }
}
