package com.shaposhnikov.facerecognizer.util;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Image.SCALE_SMOOTH;

/**
 * Created by Kirill on 12.03.2017.
 */
public class ImageHelper {

    public static void saveImage(Mat image, String path, String fileName) {
        saveImage(ImageConverter.matToBufferedImage(image), path, fileName);
    }

    public static void saveImage(BufferedImage image, String path, String fileName) {
        try {
            ImageIO.write(image, "png", new File(path + fileName));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save image with file " + fileName, e);
        }
    }

    public static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
        java.awt.Image tmp = img.getScaledInstance(newW, newH, SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
