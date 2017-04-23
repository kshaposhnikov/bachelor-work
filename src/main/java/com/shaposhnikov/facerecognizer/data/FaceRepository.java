package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kirill on 23.04.2017.
 */
public interface FaceRepository extends MongoRepository<Face, String> {
}
