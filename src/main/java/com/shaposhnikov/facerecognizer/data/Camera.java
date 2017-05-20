/**
 * Created by Kirill on 14.04.2017.
 */
package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "cameras")
public class Camera {

    @Id
    private String objectId;

    @Field("name")
    private String name;

    @Field("address")
    private String address;

    @Field("description")
    private String description;

    @Field("successCall")
    private String successCall;

    @Field("erroneousCall")
    private String erroneousCall;

    public Camera() {
        // Required for Spring
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public String getSuccessCall() {
        return successCall;
    }

    public void setSuccessCall(String successCall) {
        this.successCall = successCall;
    }

    public String getErroneousCall() {
        return erroneousCall;
    }

    public void setErroneousCall(String erroneousCall) {
        this.erroneousCall = erroneousCall;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
