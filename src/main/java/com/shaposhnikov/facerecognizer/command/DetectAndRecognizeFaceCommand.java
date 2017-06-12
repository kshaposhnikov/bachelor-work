package com.shaposhnikov.facerecognizer.command;

import com.shaposhnikov.facerecognizer.data.Camera;
import com.shaposhnikov.facerecognizer.data.History;
import com.shaposhnikov.facerecognizer.data.Human;
import com.shaposhnikov.facerecognizer.data.repository.CameraRepository;
import com.shaposhnikov.facerecognizer.data.repository.HistoryRepository;
import com.shaposhnikov.facerecognizer.data.repository.HumanRepository;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.service.RecognizedCacheController;
import com.shaposhnikov.facerecognizer.service.response.FaceResponse;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import com.shaposhnikov.facerecognizer.util.ImageHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Kirill on 16.03.2017.
 */
public class DetectAndRecognizeFaceCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(DetectAndRecognizeFaceCommand.class);

    private static final long INTERVAL = 1000 * 7;

    private final DetectFaceCommand detectFaceCommand;
    private final RecognizeContext context;
    private final HumanRepository humanRepository;
    private final HistoryRepository historyRepository;
    private final CameraRepository cameraRepository;
    private final RestTemplate restTemplate;

    private long startTime;

    public DetectAndRecognizeFaceCommand(RecognizeContext context,
                                         HumanRepository humanRepository,
                                         HistoryRepository historyRepository,
                                         CameraRepository cameraRepository) {
        this.detectFaceCommand = new DetectFaceCommand(context);
        this.context = context;
        this.humanRepository = humanRepository;
        this.historyRepository = historyRepository;
        this.cameraRepository = cameraRepository;
        this.restTemplate = new RestTemplate();
        this.startTime = System.currentTimeMillis();
    }

    public BufferedImage doWork(BufferedImage image, String cameraId) {
        Pair<Mat, Collection<Rect>> faces = detectFaceCommand.doWork(image);
        for (Rect face : faces.getValue()) {
            Mat submat = faces.getKey().submat(face);

            if (System.currentTimeMillis() - startTime > INTERVAL) {
                startTime = System.currentTimeMillis();
                new Thread(recognize(submat, cameraId)).run();
            }
        }
        return ImageConverter.matToBufferedImage(faces.getKey());
    }

    private Runnable recognize(final Mat image, final String cameraId) {
        return () -> {
            String humanId = context.getRecognizer().recognize(image);
            Camera camera = cameraRepository.findOne(cameraId);
            Human human;
            if ("-1".equals(humanId)) {
                human = new Human();
                human.setFirstName("Unknown");
                human.setLastName("Human");
                postHttpMessage(camera.getErroneousCall() + cameraId);
            } else {
                human = humanRepository.findByHumanId(humanId);
                postHttpMessage(camera.getSuccessCall() + humanId);
            }

            historyRepository.insert(buildHistoryRecord(human, camera));

            RecognizedCacheController.add(
                    cameraId,
                    new FaceResponse(
                            ImageHelper.resizeImage(ImageConverter.matToBufferedImage(image), 64, 64),
                            human
                    )
            );
        };
    }

    private void postHttpMessage(String address) {
        try {
            restTemplate.postForLocation(address, null);
        } catch (Exception e) {
            LOGGER.error("Couldn't send http request on address " + address, e);
        }
    }

    private History buildHistoryRecord(Human human, Camera camera) {
        History history = new History();
        history.setFirstName(human.getFirstName());
        history.setLastName(human.getLastName());
        history.setHumanId(human.getHumanId());
        history.setVisitDate(new Date(System.currentTimeMillis()));
        history.setCameraId(camera.getObjectId());
        history.setCameraName(camera.getName());
        return history;
    }
}
