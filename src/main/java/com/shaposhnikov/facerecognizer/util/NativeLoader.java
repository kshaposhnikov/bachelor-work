/**
 * Created by Kirill on 08.03.2017.
 */
package com.shaposhnikov.facerecognizer.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

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
            Map<String, Boolean> nativeLibraryStatuses = new HashMap<>();

            ClassLoader parent = this.getClass().getClassLoader().getParent();
            Field nativeLibraries = ClassLoader.class.getDeclaredField("nativeLibraries");
            nativeLibraries.setAccessible(true);

            Vector<Object> libraries = (Vector<Object>) nativeLibraries.get(parent);
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

            if (!nativeLibraryStatuses.containsKey(name)) {
                Properties properties = new Properties();
                properties.load(getClass().getResourceAsStream("config.properties"));

                System.load(properties.getProperty(buildPathProperty()) +
                        name +
                        properties.getProperty(buildExtenstionProperty())
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open static \"config.properties\"", e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
