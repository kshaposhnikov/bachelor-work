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
public class FileBaseRecognizeContainer implements OpenCVRecognizeContainer {

    private List<Mat> imagesToTrain = new ArrayList<>();
    private List<Integer> labels = new ArrayList<>();
    private boolean loaded = false;

    private final String pathToResult;
    private final String pathToSource;

    public FileBaseRecognizeContainer(String pathToResult, String pathToSource) {
        this.pathToResult = pathToResult;
        this.pathToSource = pathToSource;
    }

    public void load() {
        load(pathToSource, "");
    }

    private void load(String pathToSource, String parent) {
        File file = new File(pathToSource);
        for (File item : file.listFiles()) {
            try {
                if (item.isDirectory()) {
                    load(item.getAbsolutePath(), item.getName());
                } else {
                    labels.add(new Integer(parent));
                    imagesToTrain.add(ImageConverter.bufferedImageToMat(ImageIO.read(item)));
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read image file" + item, e);
            }
        }
        loaded = true;
    }

    public List<Mat> getImagesToTrain() {
        return imagesToTrain;
    }

    public Mat getLabels() {
        MatOfInt res = new MatOfInt();
        res.fromList(labels);
        return res;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public String getPathToResult() {
        return pathToResult;
    }
}
