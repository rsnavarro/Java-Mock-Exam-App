//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.model;

import java.time.LocalDateTime;
import java.util.List;

public class ExamResult {
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private double score;
    private double percentage;
    private LocalDateTime examDate;
    private long durationInMinutes;
    private List<QuestionResult> questionResults;
    private boolean passed;
    private double passingScore;

    public ExamResult() {
    }

    public ExamResult(int totalQuestions, int correctAnswers, double passingScore) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = totalQuestions - correctAnswers;
        this.score = (double)correctAnswers;
        this.percentage = (double)correctAnswers * (double)100.0F / (double)totalQuestions;
        this.examDate = LocalDateTime.now();
        this.passed = this.percentage >= passingScore;
        this.passingScore = passingScore;
    }

    public int getTotalQuestions() {
        return this.totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return this.correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return this.incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getPercentage() {
        return this.percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getExamDate() {
        return this.examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public long getDurationInMinutes() {
        return this.durationInMinutes;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public List<QuestionResult> getQuestionResults() {
        return this.questionResults;
    }

    public void setQuestionResults(List<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public boolean isPassed() {
        return this.passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public double getPassingScore() {
        return this.passingScore;
    }

    public void setPassingScore(double passingScore) {
        this.passingScore = passingScore;
    }
}
