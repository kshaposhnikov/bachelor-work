/**
 * Created by Kirill on 08.03.2017.
 */
package com.shaposhnikov.facerecognizer.util;

import java.io.IOException;
import java.util.Properties;
import java.util.StringJoiner;

public class NativeLoader {

    private static final String NATIVE_LIB_PATH = "opencv.native.lib.path";
    private static final String NATIVE_LIB_EXTENSION = "opencv.native.lib.extension";

    private static final String PROPERTY_KEY_SEPARATOR = ".";

    private static NativeLoader instance;

    private NativeLoader() {}

    public static NativeLoader getInstance() {
        if (instance == null) {
            instance = new NativeLoader();
        }
        return instance;
    }

    public void load(String name) {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("config.properties"));

            System.load(properties.getProperty(buildPathProperty()) +
                            name +
                            properties.getProperty(buildExtenstionProperty())
            );
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open resource \"config.properties\"", e);
        }
    }

    private String buildPathProperty() {
        return new StringJoiner(PROPERTY_KEY_SEPARATOR).add(NATIVE_LIB_PATH)
                .add(getOsName())
                .add(getArch())
                .toString();
    }

    private String buildExtenstionProperty() {
        return new StringJoiner(PROPERTY_KEY_SEPARATOR).add(NATIVE_LIB_EXTENSION).add(getOsName()).toString();
    }

    private String getOsName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "windows";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return "linux";
        } else {
            return "mac";
        }
    }

    private String getArch() {
        return System.getProperty("os.arch");
    }

}
