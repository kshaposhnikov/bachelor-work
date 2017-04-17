/**
 * Created by Kirill on 22.03.2017.
 */
package com.shaposhnikov.facerecognizer.controller;

import com.github.sarxos.webcam.util.ImageUtils;
import com.shaposhnikov.facerecognizer.command.DetectAndRecognizeFaceCommand;
import com.shaposhnikov.facerecognizer.command.OutputImageCommand;
import com.shaposhnikov.facerecognizer.data.Camera;
import com.shaposhnikov.facerecognizer.data.CameraRepository;
import com.shaposhnikov.facerecognizer.detector.HaarFaceDetector;
import com.shaposhnikov.facerecognizer.grabber.WebCamGrabber;
import com.shaposhnikov.facerecognizer.grabber.WebCamGrabberNew;
import com.shaposhnikov.facerecognizer.recognizer.FisherFaceRecognizer;
import com.shaposhnikov.facerecognizer.recognizer.LBPHFaceRecognizer;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import com.shaposhnikov.facerecognizer.util.NativeLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping(value = "/webcam")
public class IpWebCamController {

    private final static Logger LOG = LoggerFactory.getLogger(IpWebCamController.class);

    static {
        NativeLoader.getInstance().load(Core.NATIVE_LIBRARY_NAME);
    }

    @Autowired
    public CameraRepository cameraRepository;

    @RequestMapping(value="/stream/{camId}", method = RequestMethod.GET)
    public @ResponseBody byte[] getStream(@PathVariable String camId) throws IOException {
        try {
            RecognizeContext context;
            if (CamCache.CONTEXT_CACHE.containsKey(camId)) {
                context = CamCache.CONTEXT_CACHE.get(camId);
                Mat raw = context.getGrabber().grab();
                context.getCommand().doWork(raw);

                byte[] pixels = Base64.getEncoder().encode(
                        ImageUtils.toByteArray(
                                new ImageConverter().matToBufferedImage(raw),
                                ImageUtils.FORMAT_PNG
                        )
                );

                return pixels;
            } else {
                createContext(camId);
                LOG.info("Context for camera {} created successful", camId);
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
        if (CamCache.CONTEXT_CACHE.containsKey(camId)) {
            RecognizeContext recognizeContext = CamCache.CONTEXT_CACHE.get(camId);
            recognizeContext.getGrabber().stop();
        }
    }

    @RequestMapping(value = "/getCameras", method = RequestMethod.GET)
    public @ResponseBody List<Camera> getCameras() {
        List<Camera> cameras = cameraRepository.findAll();
        return cameras;
    }

    private synchronized RecognizeContext createContext(String camId) throws MalformedURLException {
        HaarFaceDetector detector = new HaarFaceDetector();
        FisherFaceRecognizer recognizer = new FisherFaceRecognizer();
        //LBPHFaceRecognizer recognizer = new LBPHFaceRecognizer();
        recognizer.load("C:/tmp/result/resultFisher.yml");
        //DetectAndRecognizeFaceCommand command = new DetectAndRecognizeFaceCommand(detector, recognizer);
        OutputImageCommand command = new OutputImageCommand();
        RecognizeContext context = new RecognizeContext(
                //new IpWebCamGrabber(command, camId, "http://192.168.1.244:8080/video"),
                new WebCamGrabberNew(),
                detector,
                recognizer,
                command
        );
        context.getGrabber().start();
        CamCache.CONTEXT_CACHE.put(camId, context);
        return context;
    }

    private byte[] outputTestImage() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:/test.jpg"));
        return Base64.getEncoder().encode(ImageUtils.toByteArray(image, ImageUtils.FORMAT_PNG));
    }
}
