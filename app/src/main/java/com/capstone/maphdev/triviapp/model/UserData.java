package com.capstone.maphdev.triviapp.model;

public class UserData {
    // entries
    private int nbQuestionsAnswered;
    private int correctAnswers;
    private int incorrectAnswers;
    private int score;

    // constructors
    public UserData(){}

    public UserData(int nbQuestionsAnswered, int correctAnswers, int incorrectAnswers, int score) {
        this.nbQuestionsAnswered = nbQuestionsAnswered;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.score = score;
    }

    // getters & setters
    public int getNbQuestionsAnswered() {
        return nbQuestionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public int getScore() {
        return score;
    }

}
