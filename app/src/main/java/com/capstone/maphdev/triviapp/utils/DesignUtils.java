package com.capstone.maphdev.triviapp.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.capstone.maphdev.triviapp.R;

public class DesignUtils {
    // customized Snackbar
    public static void showSnackBar(View view, String text, Context context){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
