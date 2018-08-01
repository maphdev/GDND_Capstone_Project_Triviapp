package com.capstone.maphdev.triviapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.activity.QuizActivity;
import com.capstone.maphdev.triviapp.adapter.CategoriesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesListFragment extends Fragment implements CategoriesAdapter.ListItemClickListener {

    public final static String ID_CATEGORY = "idCategory";

    private CategoriesAdapter categoriesAdapter;
    private GridLayoutManager gridLayoutManager;
    @BindView(R.id.categories_adapter_recyclerView) RecyclerView recyclerView;

    public CategoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories_list, container, false);

        ButterKnife.bind(this, rootView);

        setAdapter();

        return rootView;
    }

    public void setAdapter() {
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        categoriesAdapter = new CategoriesAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoriesAdapter);
    }

    Toast toast = null;

    @Override
    public void onListItemClicked(int idCategory) {
        Intent startQuizActivity = new Intent(getContext(), QuizActivity.class);
        startQuizActivity.putExtra(ID_CATEGORY, idCategory);
        startActivity(startQuizActivity);
    }
}
