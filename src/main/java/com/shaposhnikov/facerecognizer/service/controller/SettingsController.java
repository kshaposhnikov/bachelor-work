package com.shaposhnikov.facerecognizer.service.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;
import com.shaposhnikov.facerecognizer.command.TrainOpenCVRecognizerCommand;
import com.shaposhnikov.facerecognizer.data.*;
import com.shaposhnikov.facerecognizer.data.repository.CameraRepository;
import com.shaposhnikov.facerecognizer.data.repository.FaceRepository;
import com.shaposhnikov.facerecognizer.data.repository.HistoryRepository;
import com.shaposhnikov.facerecognizer.data.repository.HumanRepository;
import com.shaposhnikov.facerecognizer.recognizer.MongoBaseRecognizeContainer;
import com.shaposhnikov.facerecognizer.service.RecognizeContext;
import com.shaposhnikov.facerecognizer.spring.ApplicationContextProvider;
import com.shaposhnikov.facerecognizer.streamserver.SFaceWebcamListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
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
    private HistoryRepository historyRepository;

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

    @RequestMapping(value = "/camera/start", method = RequestMethod.POST)
    public @ResponseBody void startCamera(@RequestParam("cameraId") final String cameraId) {
        if (CollectionUtils.isEmpty(Webcam.getWebcams()) || Webcam.getWebcams().size() == 1) {
            cameraRepository.findAll().forEach(camera -> {
                try {
                    if (!"localhost".equals(camera.getAddress()) && !IpCamDeviceRegistry.isRegistered(camera.getObjectId())) {
                        IpCamDevice device = new IpCamDevice(camera.getObjectId(), camera.getAddress(), IpCamMode.PUSH);
                        IpCamDeviceRegistry.register(device);
                        // IpCamDriver driver = new IpCamDriver();
                        //driver.register(device);
                      //  Webcam.setDriver(driver);
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Couldn't create webcam device with address " + camera.getAddress());
                }
            });
        }
        Webcam.getWebcams().forEach(device -> {
            String deviceName = device.getName();
            cameraRepository.findById(cameraId).ifPresent(registeredCamera -> {
                if ((cameraId.equals(deviceName) || deviceName.contains(registeredCamera.getName()))
                        && !device.isOpen()) {
                    device.addWebcamListener(new SFaceWebcamListener(
                                    RecognizeContext.getDefault(),
                                    humanRepository,
                                    historyRepository,
                                    cameraRepository
                            )
                    );
                    //               device.setViewSize(new Dimension(640, 480));
                    logger.info("Try to open device {} with resolution {}",
                            ((IpCamDevice) device.getDevice()).getURL(), device.getDevice().getResolution());
                    device.open(true, (snapshotDuration, deviceFps) -> Math.max(100 - snapshotDuration, 0));
                }
            });

        });
        //return "redirect:/settings";
    }

    @RequestMapping(value = "/camera/stop", method = RequestMethod.POST)
    public @ResponseBody void stopCamera(@RequestParam("cameraId") final String cameraId) {
        Webcam.getWebcams().forEach(device -> {
            cameraRepository.findById(cameraId).ifPresent(camera -> {
                if (cameraId.equals(device.getName()) || device.getName().contains(camera.getName())) {
                    device.close();
                }
            });
        });
    }

    @RequestMapping(value = "/getCameras", method = RequestMethod.GET)
    public @ResponseBody List<Camera> getCameras() {
        return cameraRepository.findAll();
    }

    @RequestMapping(value = "/getCamera", method = RequestMethod.GET)
    public @ResponseBody Camera getCamera(@RequestParam String cameraId) {
        Optional<Camera> camera = cameraRepository.findById(cameraId);
        if (camera.isPresent()) {
            return camera.get();
        }
        throw new RuntimeException("Camera [" + cameraId + "] not found");
    }

    @RequestMapping(value = "/updateCamera", method = RequestMethod.POST)
    public String updateCamera(Camera camera) {
        cameraRepository.save(camera);
        return "redirect:/settings#all_cameras";
    }

    @RequestMapping(value = "/removeCamera", method = RequestMethod.POST)
    public String removeCamera(String cameraId) {
        cameraRepository.deleteById(cameraId);
        return "redirect:/settings#all_cameras";
    }

    @RequestMapping(value = "/getHumans", method = RequestMethod.GET)
    public @ResponseBody List<Human> getHumans() {
        return humanRepository.findAll();
    }

    @RequestMapping(value = "/getHuman", method = RequestMethod.GET)
    public @ResponseBody Human getHuman(@RequestParam String humanId) {
        Optional<Human> human = humanRepository.findById(humanId);
        if (human.isPresent()) {
            return human.get();
        }
        throw new RuntimeException("Human [" + humanId + "] not found");
    }

    @RequestMapping(value = "/newPerson", method = RequestMethod.POST)
    public String addNewPerson(Human human) {
        humanRepository.insert(human);
        return "redirect:/settings#new_person";
    }

    @RequestMapping(value = "/updatePerson", method = RequestMethod.POST)
    public String updatePerson(Human human) {
        humanRepository.save(human);
        return "redirect:/settings#all_person";
    }

    @RequestMapping(value = "/removePerson", method = RequestMethod.POST)
    public String removeHuman(@RequestParam("personId") String personId) {
        humanRepository.deleteById(personId);
        return "redirect:/settings#all_person";
    }

    @RequestMapping(value = "/newCamera", method = RequestMethod.POST)
    public String addNewCamera(Camera camera) {
        cameraRepository.insert(camera);
        return "redirect:/settings#new_camera";
    }

    @RequestMapping(value = "/getPageCount", method = RequestMethod.GET)
    public @ResponseBody String getPageCount(@RequestParam("size") int size) {
        long count = historyRepository.count();
        if (count <= size) {
            return String.valueOf(1);
        } else if (count % size == 0) {
            return String.valueOf(count / size);
        } else {
            return String.valueOf((int)(count / size) + 1);
        }
    }

    @RequestMapping(value = "/getHistory", method = RequestMethod.GET)
    public @ResponseBody List<History> getHistory(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<History> all = historyRepository.findAll(new PageRequest(page - 1, size));
        return all.getContent();
    }
}