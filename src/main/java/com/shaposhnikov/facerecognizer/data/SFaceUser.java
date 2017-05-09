package com.shaposhnikov.facerecognizer.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by Kirill on 09.05.2017.
 */
@Document(collection = "users")
public class SFaceUser {

    @Id
    private String objectId;

    @Field("userName")
    private String userName;

    @Field("password")
    private String password;

    @Field("roles")
    private List<String> role;

    public SFaceUser() {
        // Required for Spring
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRole() {
        return role;
    }
}
