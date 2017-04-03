package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.updater.Updater;
import com.shaposhnikov.facerecognizer.util.ImageHelper;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collection;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndGenerateTemplatesCommand extends DetectFaceCommand {

    private final ImageHelper imageHelper;
    private final String directoryToSave;

    private long startTime = System.currentTimeMillis();

    public DetectAndGenerateTemplatesCommand(IFaceDetector<Mat> detector, String directoryToSave) {
        super(detector);
        this.imageHelper = new ImageHelper();
        this.directoryToSave = directoryToSave;
    }

    @Override
    public Collection<Rect> doWork(Mat image) {
        for (Rect face : super.doWork(image)) {
            if (System.currentTimeMillis() - startTime >= 1000) {
                Mat submat = image.submat(face);
                Imgproc.resize(submat, submat, new Size(240, 240));
                imageHelper.saveImage(submat, directoryToSave, "test_" + System.currentTimeMillis() + ".png");
                startTime = System.currentTimeMillis();
            }
        }
        return null;
    }
}
