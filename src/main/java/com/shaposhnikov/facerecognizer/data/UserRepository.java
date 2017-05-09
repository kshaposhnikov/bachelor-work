package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kirill on 09.05.2017.
 */
public interface UserRepository extends MongoRepository<SFaceUser, String> {

    SFaceUser findByUserName(String userName);
}
