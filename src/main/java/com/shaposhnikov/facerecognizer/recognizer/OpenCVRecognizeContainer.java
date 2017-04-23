package com.shaposhnikov.facerecognizer.recognizer;

import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kirill on 22.04.2017.
 */
public interface OpenCVRecognizeContainer {

    void load();

    String getPathToResult();

    List<Mat> getImagesToTrain();

    Mat getLabels();

    boolean isLoaded();
}
