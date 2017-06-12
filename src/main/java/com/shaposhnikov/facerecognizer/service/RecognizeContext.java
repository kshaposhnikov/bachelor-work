package com.shaposhnikov.facerecognizer.service;

import com.shaposhnikov.facerecognizer.detector.HaarFaceDetector;
import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.recognizer.FisherFaceRecognizer;
import com.shaposhnikov.facerecognizer.recognizer.IOpenCVFaceRecognizer;
import org.opencv.core.Mat;

/**
 * Created by Kirill on 28.03.2017.
 */
public class RecognizeContext {

    private final IFaceDetector<Mat> detector;
    private final IOpenCVFaceRecognizer recognizer;

    public RecognizeContext(IFaceDetector<Mat> detector, IOpenCVFaceRecognizer recognizer) {
        this.detector = detector;
        this.recognizer = recognizer;
    }

    public IFaceDetector<Mat> getDetector() {
        return detector;
    }

    public IOpenCVFaceRecognizer getRecognizer() {
        return recognizer;
    }

    public static RecognizeContext getDefault() {
        return new RecognizeContext(
                new HaarFaceDetector(),
                new FisherFaceRecognizer("C:/tmp/result/resultFisher.yml")
        );
    }
}
