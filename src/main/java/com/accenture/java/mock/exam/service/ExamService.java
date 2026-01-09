//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.service;

import com.accenture.java.mock.exam.model.ExamResult;
import com.accenture.java.mock.exam.model.Question;
import com.accenture.java.mock.exam.model.QuestionResult;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    @Autowired
    private CsvReaderService csvReaderService;
    private static final double DEFAULT_PASSING_SCORE = (double)70.0F;

    public List<Question> startExam(String csvFilePath, int numberOfQuestions, boolean randomize) {
        try {
            List<Question> allQuestions = this.csvReaderService.readQuestionsFromCsv(csvFilePath);
            return this.prepareExamQuestions(allQuestions, numberOfQuestions, randomize);
        } catch (Exception e) {
            System.err.println("Error loading questions from CSV: " + e.getMessage());
            return new ArrayList();
        }
    }

    public List<Question> startExamFromResource(String resourcePath, int numberOfQuestions, boolean randomize) {
        try {
            List<Question> allQuestions = this.csvReaderService.readQuestionsFromClasspath(resourcePath);
            return this.prepareExamQuestions(allQuestions, numberOfQuestions, randomize);
        } catch (Exception e) {
            System.err.println("Error loading questions from resource: " + e.getMessage());
            return new ArrayList();
        }
    }

    public List<Question> filterByCategory(List<Question> questions, String category) {
        return (List)questions.stream().filter((q) -> q.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<Question> filterByCategories(List<Question> questions, String categories) {
        if (categories != null && !categories.trim().isEmpty()) {
            Set<String> categorySet = (Set)Arrays.stream(categories.split(",")).map(String::trim).filter((cat) -> !cat.isEmpty()).map(String::toLowerCase).collect(Collectors.toSet());
            return categorySet.isEmpty() ? questions : (List)questions.stream().filter((q) -> categorySet.contains(q.getCategory().toLowerCase())).collect(Collectors.toList());
        } else {
            return questions;
        }
    }

    public ExamResult calculateResult(List<Question> questions, List<Character> userAnswers, LocalDateTime startTime, LocalDateTime endTime) {
        int correctCount = 0;
        List<QuestionResult> questionResults = new ArrayList();

        for(int i = 0; i < questions.size(); ++i) {
            Question question = (Question)questions.get(i);
            char userAnswer = i < userAnswers.size() ? (Character)userAnswers.get(i) : 32;
            QuestionResult questionResult = new QuestionResult(question, userAnswer);
            questionResults.add(questionResult);
            if (questionResult.isCorrect()) {
                ++correctCount;
            }
        }

        ExamResult result = new ExamResult(questions.size(), correctCount, (double)70.0F);
        result.setQuestionResults(questionResults);
        result.setExamDate(startTime);
        if (startTime != null && endTime != null) {
            long duration = ChronoUnit.MINUTES.between(startTime, endTime);
            result.setDurationInMinutes(duration);
        }

        return result;
    }

    public Set<String> getAvailableCategories(List<Question> questions) {
        Set<String> categories = new HashSet();

        for(Question question : questions) {
            categories.add(question.getCategory());
        }

        return categories;
    }

    private List<Question> prepareExamQuestions(List<Question> allQuestions, int numberOfQuestions, boolean randomize) {
        List<Question> examQuestions = new ArrayList(allQuestions);
        if (randomize) {
            Collections.shuffle(examQuestions);
        }

        if (numberOfQuestions > 0 && numberOfQuestions < examQuestions.size()) {
            examQuestions = examQuestions.subList(0, numberOfQuestions);
        }

        return examQuestions;
    }

    public boolean isValidAnswer(String answer) {
        if (answer != null && answer.trim().length() == 1) {
            char choice = answer.trim().toUpperCase().charAt(0);
            return choice == 'A' || choice == 'B' || choice == 'C' || choice == 'D';
        } else {
            return false;
        }
    }
}
