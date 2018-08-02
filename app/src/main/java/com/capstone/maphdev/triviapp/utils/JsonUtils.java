package com.capstone.maphdev.triviapp.utils;

import com.capstone.maphdev.triviapp.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String RESULTS = "results";

    // parse the json get from the API
    public static List<Question> parseJson(String json){

        // retrieve the results array
        JSONObject jsonObject;
        JSONArray jsonArray;
        String listJson = null;

        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.optJSONArray(RESULTS);
            listJson = jsonArray.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        // parse the JSON into a tab of questions
        Gson gson = new GsonBuilder().create();
        Question[] questionsTab = gson.fromJson(listJson, Question[].class);

        // transform the tab into a list and decoding base64 format to utf-8
        List<Question> questionsList = new ArrayList<>();
        for (Question q : questionsTab) {

            q.setCategory(StringUtils.base64ToUTF8(q.getCategory()));
            q.setType(StringUtils.base64ToUTF8(q.getType()));
            q.setDifficulty(StringUtils.base64ToUTF8(q.getDifficulty()));
            q.setQuestion(StringUtils.base64ToUTF8(q.getQuestion()));
            q.setCorrect_answer(StringUtils.base64ToUTF8(q.getCorrect_answer()));

            List<String> listIncorrectAnswer = new ArrayList<>();
            for (int j = 0; j < q.getIncorrect_answers().size(); j++) {
                listIncorrectAnswer.add(StringUtils.base64ToUTF8(q.getIncorrect_answers().get(j)));
            }

            q.setIncorrect_answers(listIncorrectAnswer);

            questionsList.add(q);
        }

        return questionsList;
    }

}
