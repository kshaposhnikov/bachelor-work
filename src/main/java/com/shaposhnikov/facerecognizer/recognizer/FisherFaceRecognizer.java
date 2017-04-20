package com.shaposhnikov.facerecognizer.recognizer;

import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.face.BasicFaceRecognizer;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Kirill on 08.03.2017.
 */
public class FisherFaceRecognizer extends AbstractOpenCVFaceRecognizer {

    public FisherFaceRecognizer() {
        this(StringUtils.EMPTY);
    }

    public FisherFaceRecognizer(String configurationFile) {
        super(Face.createFisherFaceRecognizer(), configurationFile);
        setThreshold(1800.0);
    }

    @Override
    public String recognize(Mat image) {
        Mat submat = image.clone();
        Imgproc.resize(submat, submat, new Size(240, 240));
        Imgproc.cvtColor(submat, submat, Imgproc.COLOR_BGR2GRAY);
        return super.recognize(submat);
    }

    @Override
    public void train(List<Mat> images, Mat labels) {
        for (Mat image : images) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        }
        super.train(images, labels);
    }

    @Override
    protected void setThreshold(double threshold) {
        ((BasicFaceRecognizer) recognizer).setThreshold(threshold);
    }
}
