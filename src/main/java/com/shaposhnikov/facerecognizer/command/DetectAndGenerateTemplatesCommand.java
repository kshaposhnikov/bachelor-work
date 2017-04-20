package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.detector.IFaceDetector;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.updater.Updater;
import com.shaposhnikov.facerecognizer.util.ImageHelper;
import javafx.scene.image.Image;
import org.apache.commons.lang3.tuple.Pair;
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

    public DetectAndGenerateTemplatesCommand(RecognizeContext context, String directoryToSave) {
        super(context);
        this.imageHelper = new ImageHelper();
        this.directoryToSave = directoryToSave;
    }

    @Override
    public Pair<Mat, Collection<Rect>> doWork() {
        Pair<Mat, Collection<Rect>> faces = super.doWork();
        for (Rect face : faces.getValue()) {
            if (System.currentTimeMillis() - startTime >= 1000) {
                Mat submat = faces.getKey().submat(face);
                Imgproc.resize(submat, submat, new Size(240, 240));
                imageHelper.saveImage(submat, directoryToSave, "test_" + System.currentTimeMillis() + ".png");
                startTime = System.currentTimeMillis();
            }
        }
        return null;
    }
}
