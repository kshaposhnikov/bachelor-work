package com.shaposhnikov.facerecognizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Kirill on 28.03.2017.
 */

@Controller
public class ServicePageController {

    @RequestMapping(value={"/","/main"}, method = RequestMethod.GET)
    public String getHomePage() {
        return "main";
    }

    @RequestMapping(value="/video", method = RequestMethod.GET)
    public String getVideoPage() {
        return "video";
    }

    @RequestMapping(value="/image", method = RequestMethod.GET)
    public String getImagePage() {
        return "image";
    }
}
