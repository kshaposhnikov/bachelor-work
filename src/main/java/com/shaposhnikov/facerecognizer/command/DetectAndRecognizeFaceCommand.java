package com.shaposhnikov.facerecognizer.command;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.data.HumanRepository;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.service.RecognizedCacheController;
import com.shaposhnikov.facerecognizer.service.response.FaceResponse;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import com.shaposhnikov.facerecognizer.util.ImageHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Collection;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndRecognizeFaceCommand implements Command<byte[]> {

    private final DetectFaceCommand detectFaceCommand;
    private final RecognizeContext context;
    private final String cameraId;
    private final HumanRepository humanRepository;

    public DetectAndRecognizeFaceCommand(RecognizeContext context, HumanRepository humanRepository, String cameraId) {
        this.detectFaceCommand = new DetectFaceCommand(context);
        this.context = context;
        this.cameraId = cameraId;
        this.humanRepository = humanRepository;
    }

    @Override
    public byte[] doWork() {
        Pair<Mat, Collection<Rect>> faces = detectFaceCommand.doWork();
        for (Rect face : faces.getValue()) {
            Mat submat = faces.getKey().submat(face);
            new Thread(recognize(submat)).run();
        }
        return ImageUtils.toByteArray(ImageConverter.matToBufferedImage(faces.getKey()), ImageUtils.FORMAT_PNG);
    }

    private Runnable recognize(final Mat image) {
        return () -> RecognizedCacheController.add(
                cameraId,
                new FaceResponse(
                        ImageHelper.resizeImage(ImageConverter.matToBufferedImage(image), 64, 64),
                        humanRepository.findByHumanId(context.getRecognizer().recognize(image))
                )
        );
    }
}
