package com.capstone.maphdev.triviapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>{

    private static final int DIFFERENCE_POSITION_VS_ID = 9;
    private static final int DIFFERENCE_POSITION_VS_RANDOM = 1;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categories_adapter_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.itemImageView.setImageResource(R.drawable.triviapp);
        holder.itemTextView.setText(Question.categoriesName.get(position));

    }

    @Override
    public int getItemCount() {
        return Question.categoriesName.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categories_adapter_item_imageView) ImageView itemImageView;
        @BindView(R.id.categories_adapter_item_textView)  TextView itemTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
