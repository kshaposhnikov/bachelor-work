package com.shaposhnikov.facerecognizer.service.controller;

import com.shaposhnikov.facerecognizer.spring.ApplicationContextProvider;
import com.shaposhnikov.facerecognizer.command.TrainOpenCVRecognizerCommand;
import com.shaposhnikov.facerecognizer.data.Camera;
import com.shaposhnikov.facerecognizer.data.CameraRepository;
import com.shaposhnikov.facerecognizer.data.Human;
import com.shaposhnikov.facerecognizer.data.HumanRepository;
import com.shaposhnikov.facerecognizer.recognizer.MongoBaseRecognizeContainer;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Kirill on 22.04.2017.
 */

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private ApplicationContextProvider applicationContext;

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @RequestMapping(value = "/updateRecognizer", method = RequestMethod.POST)
    public String updateRecognizer() {
        try {
            MongoBaseRecognizeContainer container = applicationContext.getApplicationContext().getBean(MongoBaseRecognizeContainer.class);
            TrainOpenCVRecognizerCommand trainCommand = new TrainOpenCVRecognizerCommand(RecognizeContext.getDefault(), container);
            trainCommand.doWork();
            return "Done!";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/newPerson", method = RequestMethod.POST)
    public void addNewPerson(Human human) {
        humanRepository.insert(human);
    }

    @RequestMapping(value = "/newCamera", method = RequestMethod.POST)
    public void addNewCamera(Camera camera) {
        cameraRepository.insert(camera);
    }
}
