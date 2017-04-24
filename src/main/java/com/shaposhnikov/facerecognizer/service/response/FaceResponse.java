package com.shaposhnikov.facerecognizer.service.response;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.data.Human;

import java.awt.image.BufferedImage;

/**
 * Created by Kirill on 24.04.2017.
 */
public class FaceResponse {

    private final byte[] face;
    private final Human human;

    public FaceResponse(BufferedImage face, Human human) {
        this.face = ImageUtils.toByteArray(face, ImageUtils.FORMAT_PNG);
        this.human = human;
    }

    public byte[] getFace() {
        return face;
    }

    public Human getHuman() {
        return human;
    }
}
