package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kirill on 16.04.2017.
 */
public interface HumanRepository extends MongoRepository<Human, String> {

    Human findByHumanId(String humanId);
}
