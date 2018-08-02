package com.capstone.maphdev.triviapp.utils;

import com.google.firebase.database.FirebaseDatabase;

public class DataUtils {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
