package com.capstone.maphdev.triviapp.model;

public class UserData {
    // entries
    private int nbQuestionsAnswered;
    private int correctAnswers;
    private int incorrectAnswers;
    private int score;

    // constructors
    public UserData(){}

    public UserData(int nbQuestionsAnswered, int correctAnswers, int incorrectAnswer, int score) {
        this.nbQuestionsAnswered = nbQuestionsAnswered;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.score = score;
    }

    // getters & setters
    public int getNbQuestionsAnswered() {
        return nbQuestionsAnswered;
    }

    public void setNbQuestionsAnswered(int nbQuestionsAnswered) {
        this.nbQuestionsAnswered = nbQuestionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
