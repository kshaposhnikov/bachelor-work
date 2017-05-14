package com.shaposhnikov.facerecognizer.data.repository;

import com.shaposhnikov.facerecognizer.data.Camera;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kirill on 14.04.2017.
 */
public interface CameraRepository extends MongoRepository<Camera, String> {

    Camera findByName(String name);
}
