package com.capstone.maphdev.triviapp.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.Question;
import com.capstone.maphdev.triviapp.model.UserData;
import com.capstone.maphdev.triviapp.utils.DataUtils;
import com.capstone.maphdev.triviapp.utils.JsonUtils;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.capstone.maphdev.triviapp.widget.QuizWidgetProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.answer1) Button answer1Btn;
    @BindView(R.id.answer2) Button answer2Btn;
    @BindView(R.id.answer3) Button answer3Btn;
    @BindView(R.id.answer4) Button answer4Btn;
    @BindView(R.id.nextQuestion) Button nextQuestionBtn;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private static Question q;
    private int nbTry = 0;

    // Firebase
    DatabaseReference thisUserRef;

    // Required empty public constructor
    public QuizFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // check if there is an internet connection
        if (!NetworkUtils.isNetworkAvailable(getContext())){
            return inflater.inflate(R.layout.no_internet_connection, container, false);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        // bind the views
        ButterKnife.bind(this, rootView);

        // initialize a reference to the user's data in firebase
        try {
            thisUserRef = DataUtils.getDatabase().getReference().child(DataUtils.USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (Exception e){
            e.printStackTrace();
        }

        // setOnClickListeners
        answer1Btn.setOnClickListener(this);
        answer2Btn.setOnClickListener(this);
        answer3Btn.setOnClickListener(this);
        answer4Btn.setOnClickListener(this);
        nextQuestionBtn.setOnClickListener(this);

        // Load a new question
        int idCategory = getActivity().getIntent().getIntExtra(CategoriesListFragment.ID_CATEGORY, 9);

        q = null;
        if (idCategory == 0){
            new GetRandomQuestionAsyncTask().execute();
        } else {
            new GetCategoryQuestionAsyncTask().execute(idCategory);
        }



        return rootView;

    }

    // AsyncTask in order to get a random question
    public class GetRandomQuestionAsyncTask extends AsyncTask<Void, Void, List<Question>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Question> doInBackground(Void... voids) {
            List<Question> questionsList = new ArrayList<>();
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrlGeneralQuestions(1));
                questionsList = JsonUtils.parseJson(response);
            } catch (Exception e){
                e.printStackTrace();
            }

            return questionsList;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);
            progressBar.setVisibility(View.GONE);
            onPostExecuteForAsyncTaskHandling(questions);
        }
    }

    // AsyncTask in order to get a question by category
    public class GetCategoryQuestionAsyncTask extends AsyncTask<Integer, Void, List<Question>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Question> doInBackground(Integer... ints) {
            Integer category = ints[0];
            List<Question> questionsList = new ArrayList<>();
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrlbyCategoryAndAmount(category, 1));
                questionsList = JsonUtils.parseJson(response);
            } catch (Exception e){
                e.printStackTrace();
            }

            return questionsList;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);
            progressBar.setVisibility(View.GONE);
            onPostExecuteForAsyncTaskHandling(questions);
        }
    }

    // to avoid duplication of code in asynctasks
    public void onPostExecuteForAsyncTaskHandling(List<Question> questions){
        question.setVisibility(View.VISIBLE);
        answer1Btn.setVisibility(View.VISIBLE);
        answer2Btn.setVisibility(View.VISIBLE);
        answer3Btn.setVisibility(View.VISIBLE);
        answer4Btn.setVisibility(View.VISIBLE);

        q = questions.get(0);

        List<String> answers = Arrays.asList(q.getCorrect_answer(), q.getIncorrect_answers().get(0), q.getIncorrect_answers().get(1), q.getIncorrect_answers().get(2));
        Collections.shuffle(answers);

        // set Title
        getActivity().setTitle(q.getCategory());

        // set button's text
        question.setText(q.getQuestion());
        answer1Btn.setText(answers.get(0));
        answer2Btn.setText(answers.get(1));
        answer3Btn.setText(answers.get(2));
        answer4Btn.setText(answers.get(3));
    }

    // handle a click on an answer
    @Override
    public void onClick(View view) {
        if (!NetworkUtils.isNetworkAvailable(getContext())){
            // shorcut : onNextQuestionClick() will replace the actual quiz fragment by a new one
            // and in the onCreateView methode of the quiz fragment, if the network is not available
            // then it shows the "no internet connection" layout
            onNextQuestionListener.onNextQuestionClick();
            return;
        }
        switch (view.getId()){
            case R.id.answer1:
                giveResult(answer1Btn);
                break;
            case R.id.answer2:
                giveResult(answer2Btn);
                break;
            case R.id.answer3:
                giveResult(answer3Btn);
                break;
            case R.id.answer4:
                giveResult(answer4Btn);
                break;
            case R.id.nextQuestion:
                onNextQuestionListener.onNextQuestionClick();
                break;
        }
    }

    // display the ui result changes, depending on the clicked button, and update the firebase database
    private void giveResult(Button btnClicked){

        incrementNbQuestionAnswered();

        if (nbTry == 0) {
            if (btnClicked.getText().toString().equals(q.getCorrect_answer())) {
                btnClicked.setBackgroundColor(getResources().getColor(R.color.colorRight));
                incrementCorrectAnswersAndScore();
            } else {
                btnClicked.setBackgroundColor(getResources().getColor(R.color.colorWrong));
                getButtonFromAnswer().setBackgroundColor(getResources().getColor(R.color.colorRight));
                incrementIncorrectAnswers();
            }
            nbTry += 1;
        }
        nextQuestionBtn.setVisibility(View.VISIBLE);
    }

    // gets the reference to the button that displays the correct answer
    private Button getButtonFromAnswer(){
        if (answer1Btn.getText().toString().equals(q.getCorrect_answer())){
            return answer1Btn;
        } else if (answer2Btn.getText().toString().equals(q.getCorrect_answer())){
            return answer2Btn;
        } else if (answer3Btn.getText().toString().equals(q.getCorrect_answer())){
            return answer3Btn;
        } else {
            return answer4Btn;
        }
    }

    // DATABASE handling when answering a question
    public void incrementNbQuestionAnswered(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child(DataUtils.NB_QUESTIONS_ANSWERED).setValue(userData.getNbQuestionsAnswered()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get data " +
                        ""+databaseError);
            }
        });
    }

    public void incrementCorrectAnswersAndScore(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child(DataUtils.CORRECT_ANSWERS).setValue(userData.getCorrectAnswers()+1);
                thisUserRef.child(DataUtils.SCORE).setValue(userData.getScore()+1);
                QuizWidgetProvider.sendBroadCast(getContext(), QuizWidgetProvider.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get data " +
                        ""+databaseError);
            }
        });
    }

    public void incrementIncorrectAnswers(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child(DataUtils.INCORRECT_ANSWERS).setValue(userData.getIncorrectAnswers()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get data " +
                        ""+databaseError);
            }
        });
    }

    // So we can display a new question, by switching fragment in the QuizActivity
    OnNextQuestionListener onNextQuestionListener;

    public interface OnNextQuestionListener{
        void onNextQuestionClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onNextQuestionListener = (OnNextQuestionListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnNextQuestionListener");
        }
    }
}
