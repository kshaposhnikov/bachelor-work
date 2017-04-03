/**
 * Created by Kirill on 13.03.2017.
 */
package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.recognizer.IOpenCVFaceRecognizer;
import com.shaposhnikov.facerecognizer.recognizer.OpenCVRecognizeContainer;

public class TrainOpenCVRecognizerCommand implements Command <Void, OpenCVRecognizeContainer> {

    private IOpenCVFaceRecognizer recognizer;

    public TrainOpenCVRecognizerCommand(IOpenCVFaceRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    @Override
    public Void doWork(OpenCVRecognizeContainer container) {
        recognizer.train(container.getImagesToTrain(), container.getLabels());
        recognizer.save(container.getPathToResult());
        return null;
    }
}
