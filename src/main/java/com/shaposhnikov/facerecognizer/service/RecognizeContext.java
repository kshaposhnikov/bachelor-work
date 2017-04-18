package com.shaposhnikov.facerecognizer.service;

import com.shaposhnikov.facerecognizer.command.Command;
import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.grabber.IFrameGrabber;
import com.shaposhnikov.facerecognizer.recognizer.IOpenCVFaceRecognizer;
import org.opencv.core.Mat;

/**
 * Created by Kirill on 28.03.2017.
 */
public class RecognizeContext {

    private final IFrameGrabber grabber;
    private final IFaceDetector<Mat> detector;
    private final IOpenCVFaceRecognizer recognizer;
    private final Command command;

    public RecognizeContext(IFrameGrabber grabber, IFaceDetector<Mat> detector, IOpenCVFaceRecognizer recognizer, Command command) {
        this.grabber = grabber;
        this.detector = detector;
        this.recognizer = recognizer;
        this.command = command;
    }

    public IFrameGrabber getGrabber() {
        return grabber;
    }

    public IFaceDetector<Mat> getDetector() {
        return detector;
    }

    public IOpenCVFaceRecognizer getRecognizer() {
        return recognizer;
    }

    public Command getCommand() {
        return command;
    }
}
