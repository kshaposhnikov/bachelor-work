package com.shaposhnikov.facerecognizer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.shaposhnikov.facerecognizer.util.NativeLoader;
import com.shaposhnikov.facerecognizer.util.PropertiesRepository;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by Kirill on 15.04.2017.
 */

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    static {
        PropertiesRepository.getInstance().load("C:/tmp/result/config.properties", "sface-properties");
        NativeLoader.getInstance().load(Core.NATIVE_LIBRARY_NAME);
        Webcam.setDriver(new IpCamDriver());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
