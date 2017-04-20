package com.shaposhnikov.facerecognizer.command;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

/**
 * Created by Kirill on 29.03.2017.
 */
public class OutputImageCommand implements Command<byte[]> {

    private final ImageConverter converter;
    private final RecognizeContext context;

    public OutputImageCommand(RecognizeContext context) {
        this.converter = new ImageConverter();
        this.context = context;
    }

    @Override
    public byte[] doWork() {
        Mat rawImage = context.getGrabber().grab();
        return ImageUtils.toByteArray(converter.matToBufferedImage(rawImage), ImageUtils.FORMAT_PNG);
    }
}
