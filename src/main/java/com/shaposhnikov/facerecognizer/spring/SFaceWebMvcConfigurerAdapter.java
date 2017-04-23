package com.shaposhnikov.facerecognizer.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Kirill on 23.04.2017.
 */
@Configuration
public class SFaceWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/video").setViewName("video");
        registry.addViewController("/image").setViewName("image");
        registry.addViewController("/settings").setViewName("settings");
    }
}
