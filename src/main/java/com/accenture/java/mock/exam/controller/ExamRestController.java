//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.controller;

import com.accenture.java.mock.exam.model.ExamResult;
import com.accenture.java.mock.exam.model.Question;
import com.accenture.java.mock.exam.service.ExamService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/exam"})
@CrossOrigin(
        origins = {"*"}
)
public class ExamRestController {
    @Autowired
    private ExamService examService;

    @GetMapping({"/questions"})
    public ResponseEntity<List<Question>> getQuestions(@RequestParam(defaultValue = "0") int count, @RequestParam(defaultValue = "true") boolean randomize, @RequestParam(required = false) String category) {
        List<Question> questions = this.examService.startExamFromResource("sample-questions.csv", 0, false);
        if (category != null && !category.isEmpty()) {
            questions = this.examService.filterByCategories(questions, category);
        }

        questions = new ArrayList(questions);
        if (count > 0 && questions.size() < 5) {
            System.out.println("Insufficient questions: " + questions.size() + " < 5");
//            return ResponseEntity.badRequest().body((Object)null);
            return ResponseEntity.badRequest().build();
        } else {
            if (randomize) {
                Collections.shuffle(questions);
                System.out.println("Questions shuffled");
            }

            if (count > 0 && count < questions.size()) {
                questions = new ArrayList(questions.subList(0, count));
                System.out.println("Limited to count: " + questions.size());
            }

            System.out.println("Final questions to return: " + questions.size());
            System.out.println("=== End Debug ===");
            return ResponseEntity.ok(questions);
        }
    }

    @PostMapping({"/submit"})
    public ResponseEntity<ExamResult> submitExam(@RequestBody ExamSubmission submission) {
        try {
            List<Question> allQuestions = this.examService.startExamFromResource("sample-questions.csv", 0, false);
            Map<Integer, Question> questionMap = (Map)allQuestions.stream().collect(Collectors.toMap(Question::getId, (q) -> q));
            List<Question> examQuestions = new ArrayList();
            List<Character> userAnswers = new ArrayList();

            for(Map.Entry<Integer, String> entry : submission.getAnswers().entrySet()) {
                Integer questionId = (Integer)entry.getKey();
                String answer = (String)entry.getValue();
                Question question = (Question)questionMap.get(questionId);
                if (question != null) {
                    examQuestions.add(question);
                    char userAnswer = answer != null && !answer.isEmpty() ? answer.charAt(0) : 32;
                    userAnswers.add(userAnswer);
                }
            }

            System.out.println("User answers processed: " + userAnswers);
            LocalDateTime startTime = submission.getStartTime();
            LocalDateTime endTime = submission.getEndTime() != null ? submission.getEndTime() : LocalDateTime.now();
            ExamResult result = this.examService.calculateResult(examQuestions, userAnswers, startTime, endTime);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error in submitExam: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/categories"})
    public ResponseEntity<List<String>> getCategories() {
        try {
            List<Question> allQuestions = this.examService.startExamFromResource("sample-questions.csv", 0, false);
            List<String> categories = this.examService.getAvailableCategories(allQuestions).stream().toList();
            return ResponseEntity.ok(categories);
        } catch (Exception var3) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/stats"})
    public ResponseEntity<ExamStats> getExamStats() {
        try {
            List<Question> allQuestions = this.examService.startExamFromResource("sample-questions.csv", 0, false);
            ExamStats stats = new ExamStats();
            stats.setTotalQuestions(allQuestions.size());
            stats.setCategories(this.examService.getAvailableCategories(allQuestions).stream().toList());
            Map<String, Long> categoryCount = (Map)allQuestions.stream().collect(Collectors.groupingBy(Question::getCategory, Collectors.counting()));
            stats.setCategoryCount(categoryCount);
            return ResponseEntity.ok(stats);
        } catch (Exception var4) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class ExamSubmission {
        private Map<Integer, String> answers;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public Map<Integer, String> getAnswers() {
            return this.answers;
        }

        public void setAnswers(Map<Integer, String> answers) {
            this.answers = answers;
        }

        public LocalDateTime getStartTime() {
            return this.startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return this.endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }

    public static class ExamStats {
        private int totalQuestions;
        private List<String> categories;
        private Map<String, Long> categoryCount;

        public int getTotalQuestions() {
            return this.totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public List<String> getCategories() {
            return this.categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }

        public Map<String, Long> getCategoryCount() {
            return this.categoryCount;
        }

        public void setCategoryCount(Map<String, Long> categoryCount) {
            this.categoryCount = categoryCount;
        }
    }
}
