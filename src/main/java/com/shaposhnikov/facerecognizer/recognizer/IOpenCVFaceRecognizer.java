package com.shaposhnikov.facerecognizer.recognizer;

import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kirill on 08.03.2017.
 */
public interface IOpenCVFaceRecognizer extends IFaceRecognizer {

    void train(List<Mat> images, Mat labels);

    void save(String fileName);

    void load(String fileName);
}
