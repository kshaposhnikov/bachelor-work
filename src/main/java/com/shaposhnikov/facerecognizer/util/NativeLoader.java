/**
 * Created by Kirill on 08.03.2017.
 */
package com.shaposhnikov.facerecognizer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class NativeLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(NativeLoader.class);

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
        if (!isLibraryLoaded(this.getClass().getClassLoader(), name)) {
            try {
                Properties properties = new Properties();
                properties.load(getClass().getResourceAsStream("config.properties"));

                System.load(properties.getProperty(buildPathProperty()) +
                        name +
                        properties.getProperty(buildExtenstionProperty())
                );

                LOGGER.debug("Library {} loaded successfully", name);
            } catch (IOException e) {
                LOGGER.error("Couldn't open static \"config.properties\"", e);
                throw new RuntimeException("Couldn't open static \"config.properties\"", e);
            }
        }
    }

    private boolean isLibraryLoaded(ClassLoader currentLoader, String name) {
        try {
            if (currentLoader != null) {
                Map<String, Boolean> nativeLibraryStatuses = new HashMap<>();
                Field nativeLibraries = ClassLoader.class.getDeclaredField("nativeLibraries");
                nativeLibraries.setAccessible(true);

                Vector<Object> libraries = (Vector<Object>) nativeLibraries.get(currentLoader);
                for (Object item : libraries) {
                    Field loadedLibraryName = item.getClass().getDeclaredField("name");
                    loadedLibraryName.setAccessible(true);

                    Field loadedLibraryStatus = item.getClass().getDeclaredField("loaded");
                    loadedLibraryStatus.setAccessible(true);

                    String loadedLibraryNameValue = (String) loadedLibraryName.get(item);
                    Boolean loadedLibraryStatusValue = loadedLibraryStatus.getBoolean(item);

                    loadedLibraryNameValue = loadedLibraryNameValue.substring(
                            loadedLibraryNameValue.lastIndexOf(File.separatorChar) + 1,
                            loadedLibraryNameValue.lastIndexOf(".dll"));

                    nativeLibraryStatuses.put(loadedLibraryNameValue, loadedLibraryStatusValue);
                }

                if (nativeLibraryStatuses.containsKey(name)) {
                    LOGGER.debug("Library {} already loaded with {}", name, currentLoader);
                    return true;
                } else {
                    return isLibraryLoaded(currentLoader.getParent(), name);
                }
            }
            return false;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Couldn't get access to the field", e);
            throw new RuntimeException("Couldn't get access to the field", e);
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
