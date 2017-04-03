package com.shaposhnikov.facerecognizer.recognizer;

import org.opencv.core.Mat;
import org.opencv.face.Face;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Kirill on 18.03.2017.
 */
public class LBPHFaceRecognizer extends AbstractOpenCVFaceRecognizer {

    public LBPHFaceRecognizer() {
        super(Face.createLBPHFaceRecognizer());
        setThreshold(175);
    }

    @Override
    public String recognize(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        return super.recognize(image);
    }

    @Override
    public void train(List<Mat> images, Mat labels) {
        images.forEach(item -> Imgproc.cvtColor(item, item, Imgproc.COLOR_BGR2GRAY));
        super.train(images, labels);
    }

    @Override
    protected void setThreshold(double threshold) {
        ((org.opencv.face.LBPHFaceRecognizer) recognizer).setThreshold(threshold);
    }
}
