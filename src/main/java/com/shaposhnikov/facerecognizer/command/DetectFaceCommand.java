package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.updater.Updater;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Collection;

/**
 * Created by Kirill on 13.03.2017.
 */
public class DetectFaceCommand implements Command<Collection<Rect>, Mat> {

    private final static Integer SHIFT_X = 10;
    private final static Integer SHIFT_Y = 20;
    private final static Integer SHIFT_WIDTH = 10;
    private final static Integer SHIFT_HEIGHT = 10;

    private final IFaceDetector<Mat> detector;

    public DetectFaceCommand(IFaceDetector<Mat> detector) {
        this.detector = detector;
    }

    @Override
    public Collection<Rect> doWork(Mat image) {
        Collection<Rect> processedFaces = detector.detect(image);
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

        return processedFaces;
    }
}
