package com.capstone.maphdev.triviapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable{

    // var
    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    // constructor
    public Question(String category, String type, String difficulty, String question, String correct_answer, List<String> incorrect_answers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    // getters & setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Category : ").append(category).append("\n");
        str.append("Type : ").append(type).append("\n");
        str.append("Difficulty : ").append(difficulty).append("\n");
        str.append("Question : ").append(question).append("\n");
        str.append("Correct answer : ").append(correct_answer).append("\n");
        for (int i = 0; i < incorrect_answers.size(); i++){
            str.append("Incorrect answer : ").append(incorrect_answers.get(i)).append("\n");
        }
        return str.toString();
    }

    public Question(Parcel in){
        this.category = in.readString();
        this.type = in.readString();
        this.difficulty = in.readString();
        this.question = in.readString();
        this.correct_answer = in.readString();
        in.readStringList(this.incorrect_answers);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(difficulty);
        dest.writeString(question);
        dest.writeString(correct_answer);
        dest.writeStringList(incorrect_answers);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>(){

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
}
