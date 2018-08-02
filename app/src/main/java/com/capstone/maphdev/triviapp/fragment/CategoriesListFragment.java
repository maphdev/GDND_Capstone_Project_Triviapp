package com.capstone.maphdev.triviapp.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.activity.QuizActivity;
import com.capstone.maphdev.triviapp.adapter.CategoriesAdapter;
import com.capstone.maphdev.triviapp.utils.DesignUtils;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesListFragment extends Fragment implements CategoriesAdapter.ListItemClickListener {

    public final static String ID_CATEGORY = "idCategory";
    private View rootView;

    @BindView(R.id.categories_adapter_recyclerView) RecyclerView recyclerView;

    public CategoriesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_categories_list, container, false);

        ButterKnife.bind(this, rootView);

        setAdapter();

        return rootView;
    }

    public void setAdapter() {
        int spanCount;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spanCount = 2;
        } else {
            spanCount = 3;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoriesAdapter);
    }

    @Override
    public void onListItemClicked(int idCategory) {
        if (!NetworkUtils.isNetworkAvailable(getContext())){
            DesignUtils.showSnackBar(rootView, getActivity().getResources().getString(R.string.no_internet_connection), getContext());
        } else {
            Intent startQuizActivity = new Intent(getContext(), QuizActivity.class);
            startQuizActivity.putExtra(ID_CATEGORY, idCategory);
            startActivity(startQuizActivity);
        }
    }
}
