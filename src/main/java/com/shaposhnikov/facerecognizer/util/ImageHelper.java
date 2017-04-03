package com.shaposhnikov.facerecognizer.util;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kirill on 12.03.2017.
 */
public class ImageHelper {

    public void saveImage(Mat image, String path, String fileName) {
        saveImage(ImageConverter.matToBufferedImage(image), path, fileName);
    }

    public void saveImage(BufferedImage image, String path, String fileName) {
        try {
            ImageIO.write(image, "png", new File(path + fileName));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save image with file " + fileName, e);
        }
    }
}
