package com.shaposhnikov.facerecognizer.streamserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.shaposhnikov.facerecognizer.command.DetectAndRecognizeFaceCommand;
import com.shaposhnikov.facerecognizer.data.repository.CameraRepository;
import com.shaposhnikov.facerecognizer.data.repository.HistoryRepository;
import com.shaposhnikov.facerecognizer.data.repository.HumanRepository;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by Kirill on 29.04.2017.
 */
public class SFaceWebcamListener implements WebcamListener {

    private final DetectAndRecognizeFaceCommand command;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public SFaceWebcamListener(RecognizeContext context, HumanRepository repository, HistoryRepository historyRepository, CameraRepository cameraRepository) {
        this.command = new DetectAndRecognizeFaceCommand(context, repository, historyRepository, cameraRepository);
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        System.out.println("here");
    }

    @Override
    public void webcamClosed(WebcamEvent we) {

    }

    @Override
    public void webcamDisposed(WebcamEvent we) {

    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        try {
            BufferedImage image = we.getImage();
            String camId = we.getSource().getName();
            if (we.getSource().getName().contains("WebCam SC-10HDD12636N")) {
                camId = "58f91ff1341801c374bc9520";
            }

            image = command.doWork(image, camId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "JPG", baos);
            String frame = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF8");

            for (WebSocketSession webSocketSession : CameraToSessionsCache.getSessions(camId)) {
                TextMessage message = new TextMessage(OBJECT_MAPPER.writeValueAsString(new CamResponse(camId, frame)));
                webSocketSession.sendMessage(message);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Couldn't convert bytes to Base 64");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't send message");
        }
    }

    private class CamResponse {

        private String camId;
        private String frame;

        public CamResponse(String camId, String frame) {
            this.camId = camId;
            this.frame = frame;
        }

        public String getCamId() {
            return camId;
        }

        public String getFrame() {
            return frame;
        }
    }
}
