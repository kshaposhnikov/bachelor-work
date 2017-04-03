/**
 * Created by Kirill on 08.03.2017.
 */
package com.shaposhnikov.facerecognizer.recognizer;

import org.opencv.core.Mat;

public interface IFaceRecognizer {

    String recognize(Mat image);
}
