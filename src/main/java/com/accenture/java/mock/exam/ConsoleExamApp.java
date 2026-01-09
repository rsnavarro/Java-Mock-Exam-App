//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam;

import com.accenture.java.mock.exam.controller.ExamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ConsoleExamApp {
    @Autowired
    private ExamController examController;

    public static void main(String[] args) {
        System.setProperty("spring.main.web-application-type", "none");
        ConfigurableApplicationContext context = SpringApplication.run(JavaMockExamApplication.class, args);
        ConsoleExamApp consoleApp = (ConsoleExamApp)context.getBean(ConsoleExamApp.class);
        consoleApp.runConsoleExam();
        context.close();
    }

    public void runConsoleExam() {
        this.examController.startExamInterface();
        System.exit(0);
    }
}
