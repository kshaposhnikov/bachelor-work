package com.shaposhnikov.facerecognizer.command;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.data.Human;
import com.shaposhnikov.facerecognizer.data.HumanRepository;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.service.RecognizedCacheController;
import com.shaposhnikov.facerecognizer.service.response.FaceResponse;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import com.shaposhnikov.facerecognizer.util.ImageHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndRecognizeFaceCommand {

    private final DetectFaceCommand detectFaceCommand;
    private final RecognizeContext context;
    private final HumanRepository humanRepository;

    public DetectAndRecognizeFaceCommand(RecognizeContext context, HumanRepository humanRepository) {
        this.detectFaceCommand = new DetectFaceCommand(context);
        this.context = context;
        this.humanRepository = humanRepository;
    }

    public BufferedImage doWork(BufferedImage image, String cameraId) {
        Pair<Mat, Collection<Rect>> faces = detectFaceCommand.doWork(image);
        for (Rect face : faces.getValue()) {
            Mat submat = faces.getKey().submat(face);
            new Thread(recognize(submat, cameraId)).run();
        }
        //return ImageUtils.toByteArray(ImageConverter.matToBufferedImage(faces.getKey()), ImageUtils.FORMAT_PNG);
        return ImageConverter.matToBufferedImage(faces.getKey());
    }

    private Runnable recognize(final Mat image, final String cameraId) {
        return () -> {
            String humanId = context.getRecognizer().recognize(image);
            Human human = null;
            if ("-1".equals(humanId)) {
                human = new Human();
                human.setFirstName("Unknown");
                human.setLastName("Human");
            } else {
                human = humanRepository.findByHumanId(humanId);
            }

            if (!RecognizedCacheController.containsFaceForCamera(cameraId, humanId)) {
                RecognizedCacheController.add(
                        cameraId,
                        new FaceResponse(
                                ImageHelper.resizeImage(ImageConverter.matToBufferedImage(image), 64, 64),
                                human
                        )
                );
            }
        };
    }
}
