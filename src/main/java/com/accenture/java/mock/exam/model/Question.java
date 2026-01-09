//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.model;

import java.util.List;

public class Question {
    private int id;
    private String questionText;
    private List<String> options;
    private char correctAnswer;
    private String explanation;
    private String category;
    private String questionType;
    private String codeImagePath;

    public Question() {
    }

    public Question(int id, String questionText, List<String> options, char correctAnswer, String explanation, String category) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.category = category;
        this.questionType = "text";
        this.codeImagePath = null;
    }

    public Question(int id, String questionText, List<String> options, char correctAnswer, String explanation, String category, String questionType, String codeImagePath) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.category = category;
        this.questionType = questionType;
        this.codeImagePath = codeImagePath;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public char getCorrectAnswer() {
        return this.correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestionType() {
        return this.questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getCodeImagePath() {
        return this.codeImagePath;
    }

    public void setCodeImagePath(String codeImagePath) {
        this.codeImagePath = codeImagePath;
    }

    public String toString() {
        return "Question{id=" + this.id + ", questionText='" + this.questionText + "', options=" + this.options + ", correctAnswer=" + this.correctAnswer + ", category='" + this.category + "', questionType='" + this.questionType + "', codeImagePath='" + this.codeImagePath + "'}";
    }
}
