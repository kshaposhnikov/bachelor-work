package com.shaposhnikov.facerecognizer.service;

import com.shaposhnikov.facerecognizer.command.Command;
import com.shaposhnikov.facerecognizer.detector.HaarFaceDetector;
import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.grabber.IFrameGrabber;
import com.shaposhnikov.facerecognizer.grabber.IpWebCamGrabber;
import com.shaposhnikov.facerecognizer.grabber.WebCamGrabberNew;
import com.shaposhnikov.facerecognizer.recognizer.FisherFaceRecognizer;
import com.shaposhnikov.facerecognizer.recognizer.IOpenCVFaceRecognizer;
import org.opencv.core.Mat;
import org.springframework.context.annotation.Bean;

import java.net.MalformedURLException;

/**
 * Created by Kirill on 28.03.2017.
 */
//public class RecognizeContext implements AutoCloseable {
public class RecognizeContext {

    //private final IFrameGrabber grabber;
    private final IFaceDetector<Mat> detector;
    private final IOpenCVFaceRecognizer recognizer;

//    public RecognizeContext(IFrameGrabber grabber, IFaceDetector<Mat> detector, IOpenCVFaceRecognizer recognizer) {
//        this.grabber = grabber;
//        this.detector = detector;
//        this.recognizer = recognizer;
//    }

    public RecognizeContext(IFaceDetector<Mat> detector, IOpenCVFaceRecognizer recognizer) {
        this.detector = detector;
        this.recognizer = recognizer;
    }

   // public IFrameGrabber getGrabber() {
//        return grabber;
//    }

    public IFaceDetector<Mat> getDetector() {
        return detector;
    }

    public IOpenCVFaceRecognizer getRecognizer() {
        return recognizer;
    }

//    @Override
//    public void close() throws Exception {
//        grabber.stop();
//    }

    public static RecognizeContext getDefault() {
        return new RecognizeContext(
//                new WebCamGrabberNew(),
                new HaarFaceDetector(),
                new FisherFaceRecognizer("C:/tmp/result/resultFisher.yml")
                //new LBPHFaceRecognizer()
        );
    }

    //public static RecognizeContext getDefaultForRemote(String camId, String address) {
    public static RecognizeContext getDefaultForRemote() {
     //   try {
            return new RecognizeContext(
  //                  new IpWebCamGrabber(camId, address),
                    new HaarFaceDetector(),
                    new FisherFaceRecognizer("C:/tmp/result/resultFisher.yml")
                    //new LBPHFaceRecognizer()
            );
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Couldn't connect to camera " + camId);
//        }
    }
}
