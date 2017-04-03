package com.shaposhnikov.facerecognizer.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.Buffer;

/**
 * Created by Kirill on 26.02.2017.
 */
public class ImageConverter {

    public static Image bufferedImageToFxImage(BufferedImage srcImage) {
        return SwingFXUtils.toFXImage(srcImage, null);
    }

    public static Image matToImage(Mat srcImage) {
        return bufferedImageToFxImage(matToBufferedImage(srcImage));

    }

    public static Mat bufferedImageToMat(BufferedImage srcImage) {
        Mat mat = new Mat(srcImage.getHeight(), srcImage.getWidth(), CvType.CV_8UC3);
        byte[] rawImage = ((DataBufferByte) srcImage.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, rawImage);
        return mat;
    }

    public static BufferedImage matToBufferedImage(Mat original)  {
        // init
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
}
