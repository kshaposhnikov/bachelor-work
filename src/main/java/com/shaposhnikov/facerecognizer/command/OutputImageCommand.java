package com.shaposhnikov.facerecognizer.command;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

/**
 * Created by Kirill on 29.03.2017.
 */
public class OutputImageCommand implements Command<byte[], Mat> {

    private final ImageConverter converter;

    public OutputImageCommand() {
        this.converter = new ImageConverter();
    }

    @Override
    public byte[] doWork(Mat args) {
        return ImageUtils.toByteArray(converter.matToBufferedImage(args), ImageUtils.FORMAT_PNG);
    }
}
