package com.shaposhnikov.facerecognizer.service.controller;

import com.shaposhnikov.facerecognizer.command.TrainOpenCVRecognizerCommand;
import com.shaposhnikov.facerecognizer.data.*;
import com.shaposhnikov.facerecognizer.recognizer.MongoBaseRecognizeContainer;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.spring.ApplicationContextProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Kirill on 22.04.2017.
 */

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private ApplicationContextProvider applicationContext;

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    @Qualifier("getGridFsTemplate")
    private GridFsTemplate gridFsTemplate;

    @RequestMapping(value = "/updateRecognizer", method = RequestMethod.POST)
    public String updateRecognizer(@RequestParam("file") MultipartFile file) {
        try {
            ZipEntry entry;
            int humanId = 0;
            File temp = File.createTempFile("temp", null);
            file.transferTo(temp);

            ZipFile zipFile = new ZipFile(temp);

            try (ZipInputStream archive = new ZipInputStream(file.getInputStream())) {
                while ((entry = archive.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        try {
                            String[] dirs = entry.getName().split("/");
                            ArrayUtils.reverse(dirs);
                            String fileName = dirs[0];
                            humanId = Integer.parseInt(fileName);
                        } catch (NumberFormatException e) {
                            logger.error("This directory name is not assignable to Integer");
                        }
                    } else {
                        String fileName = entry.getName().substring(
                                entry.getName().lastIndexOf('/') + 1,
                                entry.getName().length()
                        );
                        if (humanId != 0) {
                            String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
                            Face face = new Face();
                            face.setExtension(extension.toUpperCase());
                            face.setIsGray("NO");
                            face.setHumanId(String.valueOf(humanId));
                            face.setFileName(fileName);
                            faceRepository.insert(face);
                            gridFsTemplate.store(zipFile.getInputStream(entry), fileName);
                        }
                    }
                }
            }
            MongoBaseRecognizeContainer container = applicationContext.getApplicationContext().getBean(MongoBaseRecognizeContainer.class);
            TrainOpenCVRecognizerCommand trainCommand = new TrainOpenCVRecognizerCommand(RecognizeContext.getDefault(), container);
            trainCommand.doWork();
            return "redirect:/settings";
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
