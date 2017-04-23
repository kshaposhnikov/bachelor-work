package com.shaposhnikov.facerecognizer.recognizer;

import com.mongodb.gridfs.GridFSDBFile;
import com.shaposhnikov.facerecognizer.data.Face;
import com.shaposhnikov.facerecognizer.data.FaceRepository;
import com.shaposhnikov.facerecognizer.data.Human;
import com.shaposhnikov.facerecognizer.data.HumanRepository;
import com.shaposhnikov.facerecognizer.util.ImageConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 22.04.2017.
 */

@Component
public class MongoBaseRecognizeContainer implements OpenCVRecognizeContainer {

    private final GridFsTemplate template;
    private final FaceRepository faceRepository;
    private final String pathToResult;

    private final List<Mat> imagesToTrain = new ArrayList<>();
    private final List<Integer> labels = new ArrayList<>();

    private boolean loaded = false;

    @Autowired
    public MongoBaseRecognizeContainer(
            FaceRepository faceRepository,
            @Qualifier("getGridFsTemplate") GridFsTemplate template) {
        this(faceRepository, template, "resultFisher.yml");
    }

    public MongoBaseRecognizeContainer(FaceRepository faceRepository,
                                       GridFsTemplate template,
                                       String pathToResult) {
        this.pathToResult = pathToResult;
        this.faceRepository = faceRepository;
        this.template = template;
    }

    @Override
    public void load() {
        for (Face face : faceRepository.findAll()) {
            labels.add(new Integer(face.getHumanId()));
            Query query = Query.query(
                    GridFsCriteria.whereFilename().is(face.getFileName())
            );
            GridFSDBFile gridFSDBFile = template.findOne(query);
            try {
                imagesToTrain.add(ImageConverter.bufferedImageToMat(ImageIO.read(gridFSDBFile.getInputStream())));
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read image from Mongo DB Grid FS with name " + face.getHumanId(), e);
            }
        }
        loaded = true;
    }

    @Override
    public String getPathToResult() {
        return pathToResult;
    }

    @Override
    public List<Mat> getImagesToTrain() {
        return imagesToTrain;
    }

    @Override
    public Mat getLabels() {
        MatOfInt res = new MatOfInt();
        res.fromList(labels);
        return res;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
