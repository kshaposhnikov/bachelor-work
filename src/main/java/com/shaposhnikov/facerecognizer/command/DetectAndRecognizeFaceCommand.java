package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.recognizer.IFaceRecognizer;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.updater.Updater;
import javafx.scene.image.Image;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Collection;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndRecognizeFaceCommand extends DetectFaceCommand {

    public DetectAndRecognizeFaceCommand(RecognizeContext context) {
        super(context);
    }

    @Override
    public Pair<Mat, Collection<Rect>> doWork() {
        Pair<Mat, Collection<Rect>> faces = super.doWork();
        for (Rect face : faces.getValue()) {
            Mat submat = faces.getKey().submat(face);
            new Thread(recognize(submat)).run();
            //System.out.println(recognizer.recognize(submat));
        }
        return faces;
    }

    private Runnable recognize(final Mat image) {
        return () -> {
            System.out.println(context.getRecognizer().recognize(image));
        };
    }
}
