package com.shaposhnikov.facerecognizer.recognizer;

import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 14.03.2017.
 */
public class OpenCVRecognizeContainer {

    private List<Mat> imagesToTrain = new ArrayList<>();
    private List<Integer> labels = new ArrayList<>();

    private final String pathToResult;

    public OpenCVRecognizeContainer(String pathToResult) {
        this.pathToResult = pathToResult;
    }

    public void load(String pathToSource) throws IOException {
        load(pathToSource, "");
    }

    private void load(String pathToSource, String parent) throws IOException {
        File file = new File(pathToSource);
        for (File item : file.listFiles()) {
            if (item.isDirectory()) {
                load(item.getAbsolutePath(), item.getName());
            } else {
                labels.add(new Integer(parent));
                imagesToTrain.add(ImageConverter.bufferedImageToMat(ImageIO.read(item)));
            }
        }
    }

    public List<Mat> getImagesToTrain() {
        return imagesToTrain;
    }

    public Mat getLabels() {
        MatOfInt res = new MatOfInt();
        res.fromList(labels);
        return res;
    }

    public String getPathToResult() {
        return pathToResult;
    }
}
