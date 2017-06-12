package com.shaposhnikov.facerecognizer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kirill on 12.06.2017.
 */
public class PropertiesRepository {

    private final static Map<String, Properties> repository = new ConcurrentHashMap<>();
    private static PropertiesRepository instance;

    private static final Object LOCK = new Object();

    private PropertiesRepository() {
    }

    public static PropertiesRepository getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new PropertiesRepository();
                }
            }
        }

        return instance;
    }

    public void load(String path, String name) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(path));
            repository.put(name, properties);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open properties file for read", e);
        }
    }

    public Properties getProperties(String repositoryName) {
        return repository.get(repositoryName);
    }

    public String getValue(String repositoryName, String key) {
        return repository.get(repositoryName).getProperty(key);
    }
}
