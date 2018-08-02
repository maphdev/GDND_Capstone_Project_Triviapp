package com.capstone.maphdev.triviapp.utils;

import com.google.firebase.database.FirebaseDatabase;

public class DataUtils {

    // useful keywords
    public static final String USERS = "users";
    public static final String SCORE = "score";
    public static final String CORRECT_ANSWERS = "correctAnswers";
    public static final String INCORRECT_ANSWERS = "incorrectAnswers";
    public static final String NB_QUESTIONS_ANSWERED = "nbQuestionsAnswered";

    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
