package com.capstone.maphdev.triviapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.adapter.CategoriesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesListFragment extends Fragment {

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
        categoriesAdapter = new CategoriesAdapter();

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoriesAdapter);
    }
}
