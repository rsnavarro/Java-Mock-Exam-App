//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.accenture.java.mock.exam.model;

public class QuestionResult {
    private Question question;
    private char userAnswer;
    private boolean correct;
    private long timeSpentInSeconds;

    public QuestionResult() {
    }

    public QuestionResult(Question question, char userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer;
        char normalizedUserAnswer = Character.toUpperCase(userAnswer);
        char normalizedCorrectAnswer = Character.toUpperCase(question.getCorrectAnswer());
        this.correct = normalizedUserAnswer == normalizedCorrectAnswer;
    }

    public QuestionResult(Question question, char userAnswer, long timeSpentInSeconds) {
        this(question, userAnswer);
        this.timeSpentInSeconds = timeSpentInSeconds;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public char getUserAnswer() {
        return this.userAnswer;
    }

    public void setUserAnswer(char userAnswer) {
        this.userAnswer = userAnswer;
        char normalizedUserAnswer = Character.toUpperCase(userAnswer);
        char normalizedCorrectAnswer = Character.toUpperCase(this.question.getCorrectAnswer());
        this.correct = normalizedUserAnswer == normalizedCorrectAnswer;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getTimeSpentInSeconds() {
        return this.timeSpentInSeconds;
    }

    public void setTimeSpentInSeconds(long timeSpentInSeconds) {
        this.timeSpentInSeconds = timeSpentInSeconds;
    }
}
