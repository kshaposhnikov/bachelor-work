package com.shaposhnikov.facerecognizer.detector;

import com.shaposhnikov.facerecognizer.util.ImageHelper;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kirill on 28.02.2017.
 */
public class HaarFaceDetector implements IFaceDetector<Mat> {

    private final Logger LOG = LoggerFactory.getLogger(HaarFaceDetector.class);

    private final MatOfRect matOfRect;
    private final CascadeClassifier cascadeClassifier;
    private final MatOfInt numOfDetect;
    private final ImageHelper imageHelper;

    private long startTime = System.currentTimeMillis();

    public HaarFaceDetector() {
        this(0);
    }

    public HaarFaceDetector(int numOfDetect) {
        this(new MatOfRect(), new CascadeClassifier(getPathToCascade()), numOfDetect);
    }

    public HaarFaceDetector(MatOfRect matOfRect, CascadeClassifier cascadeClassifier, int numOfDetect) {
        this.matOfRect = matOfRect;
        this.cascadeClassifier = cascadeClassifier;
        if (numOfDetect == 0) {
            this.numOfDetect = new MatOfInt();
        } else {
            this.numOfDetect = new MatOfInt(numOfDetect);
        }
        this.imageHelper = new ImageHelper();
    }

    public synchronized Collection<Rect> detect(Mat srcImage) {
        Mat grayImage = new Mat();
        MatOfRect rect = new MatOfRect();

        if (srcImage.empty()) {
            LOG.debug("Were got empty image");
            return Collections.EMPTY_LIST;
        }

        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        if (numOfDetect.empty()) {
            cascadeClassifier.detectMultiScale(grayImage, rect);
        } else {
            cascadeClassifier.detectMultiScale2(
                    grayImage,
                    rect,
                    new MatOfInt(numOfDetect),
                    1.1, 3, 4 | 8,
                    new Size(), new Size()
            );
            LOG.info("Apply cascade");
        }

        List<Rect> rects = rect.toList();
        LOG.info("Were found {} faces", rects.size());
        return rects;
    }

    private static String getPathToCascade() {
        String filePath = HaarFaceDetector.class.getResource("haarcascade_frontalface_alt.xml").getFile();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            filePath = filePath.replaceFirst("[/]", "");
            return filePath.replace("/", "\\");
        } else {
            return filePath;
        }
    }
}
