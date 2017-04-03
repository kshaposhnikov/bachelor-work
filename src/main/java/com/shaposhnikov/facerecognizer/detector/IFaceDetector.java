package com.shaposhnikov.facerecognizer.detector;

import org.opencv.core.Rect;

import java.util.Collection;

/**
 * Created by Kirill on 01.03.2017.
 */
public interface IFaceDetector<T> {

    Collection<Rect> detect(T srcImage);
}
