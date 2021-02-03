/**
 * Created by Kirill on 22.03.2017.
 */
package com.shaposhnikov.facerecognizer.service.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.data.Camera;
import com.shaposhnikov.facerecognizer.data.repository.CameraRepository;
import com.shaposhnikov.facerecognizer.data.repository.HumanRepository;
import com.shaposhnikov.facerecognizer.service.ContextCacheController;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.service.RecognizedCacheController;
import com.shaposhnikov.facerecognizer.service.response.CameraResponse;
import com.shaposhnikov.facerecognizer.service.response.FaceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/webcam")
public class IpWebCamController {

    private final static Logger LOG = LoggerFactory.getLogger(IpWebCamController.class);
    private final Object lock = new Object();

    private final static String DEFAULE_WEBCAM = "58f91ff1341801c374bc9520";

    @Autowired
    public CameraRepository cameraRepository;

    @Autowired
    public HumanRepository humanRepository;

    @RequestMapping(value="/start/{camId}", method = RequestMethod.GET)
    public @ResponseBody byte[] startStream(@PathVariable String camId) throws IOException {
        try {
            if (ContextCacheController.containsCam(camId)) {
//                DetectAndRecognizeFaceCommand command = new DetectAndRecognizeFaceCommand(
//                        ContextCacheController.get(camId),
//                        humanRepository,
//                        camId
//                );
                //OutputImageCommand command = new OutputImageCommand(ContextCacheController.get(camId));

                //return Base64.getEncoder().encode(command.doWork());
            }

            LOG.info("Were returned empty bytes");
            return new byte[0];
        } catch (Throwable e) {
            LOG.error("Request failed", e);
            throw new RuntimeException("Request failed", e);
        }
    }

    @RequestMapping(value = "/stop/{camId}", method = RequestMethod.POST)
    public void stopStream(@PathVariable String camId) {
        try {
            if (ContextCacheController.containsCam(camId)) {
               // ContextCacheController.get(camId).close();
            }
        } catch (Exception e) {
            LOG.error("Couldn't close context for camera " + camId, e);
        }
    }

    @RequestMapping(value = "/getFace", method = RequestMethod.GET)
    public @ResponseBody FaceResponse getRecognizedFace(@RequestParam(name = "camId") String camId) {
        return RecognizedCacheController.get(camId);
    }

    @RequestMapping(value = "/getCameras", method = RequestMethod.GET)
    public @ResponseBody List<CameraResponse> getCameras() {
        final List<CameraResponse> cameras = new ArrayList<>();
        cameraRepository.findAll().stream()
                .filter(item -> {
                    for (Webcam webcam : Webcam.getWebcams()) {
                        if ((webcam.getName().contains(item.getName()) || webcam.getName().contains(item.getObjectId())) && webcam.isOpen()) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach((Camera camera) -> {
                    cameras.add(new CameraResponse(camera.getObjectId(), camera.getName(), camera.getDescription()));
                });
        return cameras;
    }

    @RequestMapping(value = "/getCamera", method = RequestMethod.GET)
    public @ResponseBody CameraResponse getCamera(@RequestParam(name = "camId") String cameraId) {
        Optional<Camera> optionalCamera = cameraRepository.findById(cameraId);
        if (optionalCamera.isPresent()) {
            Camera camera = optionalCamera.get();
            return new CameraResponse(camera.getObjectId(), camera.getName(), camera.getDescription());
        }
        throw new RuntimeException();
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public void activateCamera(@RequestParam(name = "camId") String cameraId) throws MalformedURLException {
        synchronized (lock) {
            Camera camera = cameraRepository.findById(cameraId).get();
            if (!ContextCacheController.containsCam(cameraId)) {
                if (DEFAULE_WEBCAM.equals(camera.getObjectId())) {
                    ContextCacheController.put(cameraId, RecognizeContext.getDefault());
                } else {
                    //ContextCacheController.put(cameraId, RecognizeContext.getDefaultForRemote(cameraId, camera.getAddress()));
                }
                LOG.info("Context for camera {} created successful", cameraId);
            }

          //  ContextCacheController.get(cameraId).getGrabber().start();
        }
    }

    private byte[] outputTestImage() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:/test.jpg"));
        return Base64.getEncoder().encode(ImageUtils.toByteArray(image, ImageUtils.FORMAT_PNG));
    }
}
