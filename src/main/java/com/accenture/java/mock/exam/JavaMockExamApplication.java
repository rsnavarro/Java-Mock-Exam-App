//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam;

import com.accenture.java.mock.exam.controller.ExamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaMockExamApplication implements CommandLineRunner {
    @Autowired
    private ExamController examController;

    public static void main(String[] args) {
        SpringApplication.run(JavaMockExamApplication.class, args);
    }

    public void run(String... args) throws Exception {
        System.out.println("\n=== CONSOLE EXAM INTERFACE STARTING ===");
        System.out.println("Web interface available at: http://localhost:8080");
        System.out.println("Console interface starting below:");
        System.out.println("===============================================\n");
        this.examController.startExamInterface();
    }
}
