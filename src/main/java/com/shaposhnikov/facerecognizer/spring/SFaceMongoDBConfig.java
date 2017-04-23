package com.shaposhnikov.facerecognizer.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Created by Kirill on 23.04.2017.
 */
@Configuration
public class SFaceMongoDBConfig {

    private final MongoDbFactory factory;
    private final MongoConverter converter;

    @Autowired
    public SFaceMongoDBConfig(MongoDbFactory factory, MongoConverter converter) {
        this.factory = factory;
        this.converter = converter;
    }

    @Bean
    public GridFsTemplate getGridFsTemplate() {
        return new GridFsTemplate(factory, converter, "faces_to_training");
    }
}
