package com.example.spiritualtablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;

    LoadingDialog(Activity activity){
        this.activity = activity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismiss(){
        alertDialog.dismiss();
    }
}
