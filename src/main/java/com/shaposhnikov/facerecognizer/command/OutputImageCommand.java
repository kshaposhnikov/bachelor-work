package com.shaposhnikov.facerecognizer.command;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.util.Base64;

/**
 * Created by Kirill on 29.03.2017.
 */
public class OutputImageCommand {

    private final ImageConverter converter;
    private final RecognizeContext context;

    public OutputImageCommand(RecognizeContext context) {
        this.converter = new ImageConverter();
        this.context = context;
    }

    //@Override
    public String doWork(BufferedImage bufferedImage) {
        //Mat rawImage = context.getGrabber().grab();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(ImageUtils.toByteArray(bufferedImage, ImageUtils.FORMAT_PNG));
    }
}
