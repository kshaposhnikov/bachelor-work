package com.shaposhnikov.facerecognizer.recognizer;

import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;

import java.io.File;
import java.util.List;

/**
 * Created by Kirill on 13.03.2017.
 */
public abstract class AbstractOpenCVFaceRecognizer implements IOpenCVFaceRecognizer {

    protected final FaceRecognizer recognizer;

    public AbstractOpenCVFaceRecognizer(FaceRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    @Override
    public String recognize(Mat image) {
        return String.valueOf(recognizer.predict_label(image));
    }

    @Override
    public void train(List<Mat> images, Mat labels) {
        recognizer.train(images, labels);
    }

    @Override
    public void save(String fileName) {
        recognizer.save(fileName);
    }

    @Override
    public void load(String fileName) {
        if (!new File(fileName).exists()) {
            throw new RuntimeException("Provided file with saved configuration of recognizer not exists");
        }
        recognizer.load(fileName);
    }

    protected abstract void setThreshold(double threshold);
}
