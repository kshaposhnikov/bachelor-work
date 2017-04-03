package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.recognizer.IFaceRecognizer;
import com.shaposhnikov.facerecognizer.updater.Updater;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Collection;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndRecognizeFaceCommand extends DetectFaceCommand {

    private final IFaceRecognizer recognizer;

    public DetectAndRecognizeFaceCommand(IFaceDetector<Mat> detector, IFaceRecognizer recognizer) {
        super(detector);
        this.recognizer = recognizer;
    }

    @Override
    public Collection<Rect> doWork(Mat image) {
        Collection<Rect> faces = super.doWork(image);
        for (Rect face : faces) {
            Mat submat = image.submat(face);
            new Thread(recognize(submat)).run();
            //System.out.println(recognizer.recognize(submat));
        }
        return faces;
    }

    private Runnable recognize(final Mat image) {
        return () -> {
            System.out.println(recognizer.recognize(image));
        };
    }
}
