//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping({"/"})
    public String index() {
        return "index";
    }

    @GetMapping({"/exam"})
    public String exam() {
        return "exam";
    }

    @GetMapping({"/results"})
    public String results() {
        return "results";
    }
}
