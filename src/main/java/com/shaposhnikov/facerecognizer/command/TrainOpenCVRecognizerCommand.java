/**
 * Created by Kirill on 13.03.2017.
 */
package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.recognizer.IOpenCVFaceRecognizer;
import com.shaposhnikov.facerecognizer.recognizer.OpenCVRecognizeContainer;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;

public class TrainOpenCVRecognizerCommand implements Command <Void> {

    private RecognizeContext context;
    private final OpenCVRecognizeContainer container;

    public TrainOpenCVRecognizerCommand(RecognizeContext context, OpenCVRecognizeContainer container) {
        this.context = context;
        this.container = container;
    }

    @Override
    public Void doWork() {
        if (!container.isLoaded()) {
            container.load();
        }
        context.getRecognizer().train(container.getImagesToTrain(), container.getLabels());
        context.getRecognizer().save(container.getPathToResult());
        return null;
    }
}
