package com.capstone.maphdev.triviapp.fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.Question;
import com.capstone.maphdev.triviapp.model.UserData;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private static Question q;
    private int nbTry = 0;

    FirebaseAuth auth;
    DatabaseReference thisUserRef;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        ButterKnife.bind(this, rootView);

        answer1Btn.setOnClickListener(this);
        answer2Btn.setOnClickListener(this);
        answer3Btn.setOnClickListener(this);
        answer4Btn.setOnClickListener(this);
        nextQuestionBtn.setOnClickListener(this);


        int idCategory = getActivity().getIntent().getIntExtra(CategoriesListFragment.ID_CATEGORY, 9);

        q = null;
        if (idCategory == 0){
            q = NetworkUtils.getRandomQuestion();
        } else {
            q = NetworkUtils.getCategoryQuestion(idCategory);
        }

        thisUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        List<String> answers;
        answers = Arrays.asList(q.getCorrect_answer(), q.getIncorrect_answers().get(0), q.getIncorrect_answers().get(1), q.getIncorrect_answers().get(2));
        Collections.shuffle(answers);

        question.setText(q.getQuestion());
        answer1Btn.setText(answers.get(0));
        answer2Btn.setText(answers.get(1));
        answer3Btn.setText(answers.get(2));
        answer4Btn.setText(answers.get(3));

        return rootView;
    }

    @Override
    public void onClick(View view) {
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

    private void giveResult(Button btnClicked){

        incrementNbQuestionAnswered();

        if (nbTry == 0) {
            if (btnClicked.getText().toString() == q.getCorrect_answer()) {
                btnClicked.setBackgroundColor(getResources().getColor(R.color.colorRight));
                incrementCorrectAnswersAndScore();
            } else {
                btnClicked.setBackgroundColor(getResources().getColor(R.color.colorWrong));
                getButtonFromAnswer(q.getCorrect_answer()).setBackgroundColor(getResources().getColor(R.color.colorRight));
                incrementIncorrectAnswers();
            }
            nbTry += 1;
        }
        nextQuestionBtn.setVisibility(View.VISIBLE);
    }

    // from an answer available, gets the reference to the button that displays it
    private Button getButtonFromAnswer(String answer){
        if (answer1Btn.getText().toString() == q.getCorrect_answer()){
            return answer1Btn;
        } else if (answer2Btn.getText().toString() == q.getCorrect_answer()){
            return answer2Btn;
        } else if (answer3Btn.getText().toString() == q.getCorrect_answer()){
            return answer3Btn;
        } else {
            return answer4Btn;
        }
    }

    // DATABASE handling
    public void incrementNbQuestionAnswered(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child("nbQuestionsAnswered").setValue(userData.getNbQuestionsAnswered()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void incrementCorrectAnswersAndScore(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child("correctAnswers").setValue(userData.getCorrectAnswers()+1);
                thisUserRef.child("score").setValue(userData.getScore()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void incrementIncorrectAnswers(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                thisUserRef.child("incorrectAnswers").setValue(userData.getIncorrectAnswers()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // So we can display a new question, by switching fragment
    OnNextQuestionListener onNextQuestionListener;

    public interface OnNextQuestionListener{
        public void onNextQuestionClick();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
