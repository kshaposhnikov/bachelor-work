package com.shaposhnikov.facerecognizer.service.response;

import com.shaposhnikov.facerecognizer.data.Human;

import java.awt.image.BufferedImage;

/**
 * Created by Kirill on 24.04.2017.
 */
public class FaceResponse {

    private final BufferedImage face;
    private final Human human;

    public FaceResponse(BufferedImage face, Human human) {
        this.face = face;
        this.human = human;
    }

    public BufferedImage getFace() {
        return face;
    }

    public Human getHuman() {
        return human;
    }
}
