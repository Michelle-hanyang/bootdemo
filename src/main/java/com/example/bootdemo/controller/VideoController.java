package com.example.bootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: YANG
 * Date: 2022/7/1 11:33
 * Describe:
 */
@Controller
public class VideoController {

    @RequestMapping(value="/video/index")
    public String index(){
        System.out.println("viedoindex");
        return "page/index";
    }
    @RequestMapping(value="/wvideo/index")
    public String wvideoIndex(){
        System.out.println("wvideo");
        return "page/windex";
    }
}
