package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Kirill on 23.04.2017.
 */
@Document(collection = "faces")
public class Face {

    @Id
    private String objectId;

    @Field("fileName")
    private String fileName;

    @Field("extension")
    private String extension;

    @Field("humanId")
    private String humanId;

    @Field("isGray")
    private String isGray;

    public String getObjectId() {
        return objectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(String humanId) {
        this.humanId = humanId;
    }

    public String getIsGray() {
        return isGray;
    }

    public void setIsGray(String isGray) {
        this.isGray = isGray;
    }
}
