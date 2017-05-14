package com.shaposhnikov.facerecognizer.data.repository;

import com.shaposhnikov.facerecognizer.data.History;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kirill on 13.05.2017.
 */
public interface HistoryRepository extends MongoRepository<History, String> {

}
