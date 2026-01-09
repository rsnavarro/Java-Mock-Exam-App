//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/analytics"})
public class AnalyticsController {
    @GetMapping({"/dashboard"})
    public String dashboard() {
        return "analytics-dashboard";
    }

    @GetMapping({"/user-results"})
    public String userResults() {
        return "user-results";
    }

    @GetMapping({"/category-analysis"})
    public String categoryAnalysis() {
        return "category-analysis";
    }
}
