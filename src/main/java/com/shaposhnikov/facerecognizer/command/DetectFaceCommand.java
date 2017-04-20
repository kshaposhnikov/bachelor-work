package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.updater.Updater;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import javafx.scene.image.Image;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Kirill on 13.03.2017.
 */
public class DetectFaceCommand implements Command<Pair<Mat, Collection<Rect>>> {

    private final static Integer SHIFT_X = 10;
    private final static Integer SHIFT_Y = 20;
    private final static Integer SHIFT_WIDTH = 10;
    private final static Integer SHIFT_HEIGHT = 10;

    protected final RecognizeContext context;

    public DetectFaceCommand(RecognizeContext context) {
        this.context = context;
    }

    @Override
    public Pair<Mat, Collection<Rect>> doWork() {
        Mat image = context.getGrabber().grab();
        Collection<Rect> processedFaces = context.getDetector().detect(image);
        for (Rect face : processedFaces) {
            int oldFaceX = face.x;
            int oldFaceY = face.y;

            if (face.y - SHIFT_Y > 0 && face.x - SHIFT_X > 0 && face.x + face.width <= image.width()) {
                face.x -= SHIFT_X;
                face.y -= SHIFT_Y;
                face.width += (oldFaceX - face.x) + SHIFT_WIDTH;
                face.height += (oldFaceY - face.y) + SHIFT_HEIGHT;

                Imgproc.rectangle(
                        image,
                        new Point(face.x, face.y),
                        new Point(face.x + face.width, face.y + face.height),
                        new Scalar(0, 0, 255)
                );
            }
        }

        return new ImmutablePair<>(image, processedFaces);
    }
}
