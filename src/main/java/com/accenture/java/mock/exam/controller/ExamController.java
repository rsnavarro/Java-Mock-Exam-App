//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.controller;

import com.accenture.java.mock.exam.model.ExamResult;
import com.accenture.java.mock.exam.model.Question;
import com.accenture.java.mock.exam.model.QuestionResult;
import com.accenture.java.mock.exam.service.ExamService;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamController {
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    @Autowired
    private ExamService examService;
    private Scanner scanner;

    public ExamController() {
        this.scanner = new Scanner(System.in);
    }

    public void startExamInterface() {
        this.printWelcome();

        while(true) {
            switch (this.scanner.nextLine().trim()) {
                case "1":
                    this.takeExamFromFile();
                    break;
                case "2":
                    this.takeExamFromResource();
                    break;
                case "3":
                    this.viewExamInfo();
                    break;
                case "4":
                    this.printHelp();
                    break;
                case "5":
                    System.out.println("Thank you for using the Java Mock Exam! Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printWelcome() {
        System.out.println("============================================================");
        System.out.println("        WELCOME TO JAVA CERTIFICATION MOCK EXAM");
        System.out.println("============================================================");
        System.out.println();
    }

    private void takeExamFromFile() {
        System.out.print("Enter CSV file path: ");
        String filePath = this.scanner.nextLine().trim();
        if (filePath.isEmpty()) {
            System.out.println("File path cannot be empty!");
        } else {
            this.conductExam(filePath, false);
        }
    }

    private void takeExamFromResource() {
        this.conductExam("sample-questions.csv", true);
    }

    private void conductExam(String path, boolean isResource) {
        int numQuestions = 50;
        System.out.println("Exam will contain 50 questions.");
        System.out.println("Time limit: 1 hour (60 minutes)");
        System.out.print("Randomize questions? (y/n): ");
        boolean randomize = this.scanner.nextLine().trim().toLowerCase().startsWith("y");
        List<Question> questions;
        if (isResource) {
            questions = this.examService.startExamFromResource(path, numQuestions, randomize);
        } else {
            questions = this.examService.startExam(path, numQuestions, randomize);
        }

        if (questions.isEmpty()) {
            logger.error("No questions found or error loading questions from path: {}", path);
            System.out.println("No questions found or error loading questions!");
        } else {
            logger.info("Exam loaded successfully with {} questions", questions.size());
            System.out.println("\nExam loaded successfully!");
            System.out.println("Total questions: " + questions.size());
            System.out.println("\nInstructions:");
            System.out.println("- Answer each question by entering A, B, C, or D");
            System.out.println("- Type 'skip' to skip a question");
            System.out.println("- Type 'quit' to exit the exam");
            System.out.println("- You have 60 minutes to complete the exam");
            System.out.println("- The exam will auto-submit after 60 minutes");
            System.out.println("\nPress Enter to start the exam...");
            this.scanner.nextLine();
            LocalDateTime startTime = LocalDateTime.now();
            List<Character> userAnswers = new ArrayList();
            AtomicBoolean timeExpired = new AtomicBoolean(false);
            CompletableFuture<Void> timerTask = CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MINUTES.sleep(60L);
                    timeExpired.set(true);
                    System.out.println("\n\n⏰ TIME IS UP! The exam will be automatically submitted.");
                    System.out.println("Press Enter to continue...");
                } catch (InterruptedException var2) {
                }

            });

            for(int i = 0; i < questions.size(); ++i) {
                if (timeExpired.get()) {
                    System.out.println("Exam terminated due to time limit.");
                    break;
                }

                long elapsedMinutes = Duration.between(startTime, LocalDateTime.now()).toMinutes();
                long remainingMinutes = 60L - elapsedMinutes;
                System.out.println("\n================================================================================");
                System.out.println("Question " + (i + 1) + " of " + questions.size() + " | Time remaining: " + remainingMinutes + " minutes");
                PrintStream var10000 = System.out;
                Object var10001 = questions.get(i);
                var10000.println("Category: " + ((Question)var10001).getCategory());
                System.out.println("================================================================================");
                this.displayQuestion((Question)questions.get(i));
                String answer = this.getAnswerWithTimeout(timeExpired);
                if (timeExpired.get()) {
                    userAnswers.add(' ');
                    break;
                }

                if ("quit".equalsIgnoreCase(answer)) {
                    logger.info("Exam terminated by user at question {}", i + 1);
                    System.out.println("Exam terminated by user.");
                    timerTask.cancel(true);
                    return;
                }

                if ("skip".equalsIgnoreCase(answer)) {
                    userAnswers.add(' ');
                } else {
                    userAnswers.add(answer.toUpperCase().charAt(0));
                }
            }

            timerTask.cancel(true);
            LocalDateTime endTime = LocalDateTime.now();
            ExamResult result = this.examService.calculateResult(questions, userAnswers, startTime, endTime);
            this.displayResults(result);
        }
    }

    private void displayQuestion(Question question) {
        System.out.println("\n" + question.getQuestionText());
        System.out.println();

        for(String option : question.getOptions()) {
            System.out.println(option);
        }

        System.out.println();
    }

    private String getAnswerWithTimeout(AtomicBoolean timeExpired) {
        while(!timeExpired.get()) {
            System.out.print("Your answer (A/B/C/D, 'skip', or 'quit'): ");
            String input = this.scanner.nextLine().trim();
            if (timeExpired.get()) {
                return "timeout";
            }

            if ("quit".equalsIgnoreCase(input) || "skip".equalsIgnoreCase(input)) {
                return input;
            }

            if (this.examService.isValidAnswer(input)) {
                return input;
            }

            System.out.println("Invalid input. Please enter A, B, C, D, 'skip', or 'quit'.");
        }

        return "timeout";
    }

    private void displayResults(ExamResult result) {
        System.out.println("\n============================================================");
        System.out.println("                    EXAM RESULTS");
        System.out.println("============================================================");
        PrintStream var10000 = System.out;
        String var10001 = result.getExamDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        var10000.println("Exam Date: " + var10001);
        var10000 = System.out;
        long var11 = result.getDurationInMinutes();
        var10000.println("Duration: " + var11 + " minutes");
        var10000 = System.out;
        int var12 = result.getTotalQuestions();
        var10000.println("Total Questions: " + var12);
        var10000 = System.out;
        var12 = result.getCorrectAnswers();
        var10000.println("Correct Answers: " + var12);
        var10000 = System.out;
        var12 = result.getIncorrectAnswers();
        var10000.println("Incorrect Answers: " + var12);
        System.out.printf("Score: %.1f%%\n", result.getPercentage());
        System.out.printf("Status: %s\n", result.isPassed() ? "PASSED ✓" : "FAILED ✗");
        System.out.printf("Passing Score: %.1f%%\n", result.getPassingScore());
        System.out.println("\n============================================================");
        System.out.println("                 DETAILED REVIEW");
        System.out.println("============================================================");

        for(int i = 0; i < result.getQuestionResults().size(); ++i) {
            QuestionResult qr = (QuestionResult)result.getQuestionResults().get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + (qr.isCorrect() ? "✓ CORRECT" : "✗ INCORRECT"));
            System.out.println(qr.getQuestion().getCategory());
            var10000 = System.out;
            Object var15 = qr.getUserAnswer() == ' ' ? "SKIPPED" : qr.getUserAnswer();
            var10000.println("Your Answer: " + var15);
            System.out.println("Question: " + qr.getQuestion().getQuestionText());
            System.out.println();
            System.out.println("Options:");

            for(String option : qr.getQuestion().getOptions()) {
                System.out.println(option);
            }

            System.out.println("Correct Answer: " + qr.getQuestion().getCorrectAnswer());
            if (!qr.isCorrect() && !qr.getQuestion().getExplanation().isEmpty()) {
                System.out.println("Explanation: " + qr.getQuestion().getExplanation());
            }
        }

        System.out.println("\n============================================================");
        System.out.print("Press Enter to continue...");
        this.scanner.nextLine();
    }

    private void viewExamInfo() {
        System.out.println("\n==================================================");
        System.out.println("              EXAM INFORMATION");
        System.out.println("==================================================");
        System.out.println("CSV Format Required:");
        System.out.println("id,question,optionA,optionB,optionC,optionD,correctAnswer,explanation,category");
        System.out.println();
        System.out.println("Example CSV line:");
        System.out.println("1,\"What is Java?\",\"A programming language\",\"A coffee\",\"An island\",\"A framework\",A,\"Java is a programming language\",\"Java Basics\"");
        System.out.println();
        System.out.println("Supported answer choices: A, B, C, D");
        System.out.println("Default passing score: 70%");
        System.out.println();
        System.out.print("Press Enter to continue...");
        this.scanner.nextLine();
    }

    private void printHelp() {
        System.out.println("\n==================================================");
        System.out.println("                    HELP");
        System.out.println("==================================================");
        System.out.println("1. Prepare your CSV file with questions");
        System.out.println("2. Select 'Take exam from CSV file' and provide file path");
        System.out.println("3. Choose number of questions and randomization");
        System.out.println("4. Answer questions by typing A, B, C, or D");
        System.out.println("5. Review your results at the end");
        System.out.println();
        System.out.println("Special commands during exam:");
        System.out.println("- 'skip' - Skip current question");
        System.out.println("- 'quit' - Exit exam early");
        System.out.println();
        System.out.print("Press Enter to continue...");
        this.scanner.nextLine();
    }
}
