package com.example.spiritualtablet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.spiritualtablet.dashboard.AudioActivity;
import com.example.spiritualtablet.dashboard.EnglishBookList;
import com.example.spiritualtablet.dashboard.Gallery;
import com.example.spiritualtablet.dashboard.NewsLetters;
import com.example.spiritualtablet.dashboard.TeluguBookList;
import com.example.spiritualtablet.dashboard.VideoList;


public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }

    //this method shuts application when back is pressed
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void openBooks(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);

        builder.setTitle("Select language");

        builder.setIcon(R.drawable.book);

        builder.setPositiveButton("Telugu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(DashBoard.this, "Please wait books are loading...",Toast.LENGTH_LONG).show();
                LoadingDialog loadingDialog = new LoadingDialog(DashBoard.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoard.this, TeluguBookList.class));
                loadingDialog.dismiss();
            }
        });

        builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(DashBoard.this, "Please wait books are loading...",Toast.LENGTH_LONG).show();
                LoadingDialog loadingDialog = new LoadingDialog(DashBoard.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoard.this, EnglishBookList.class));
                loadingDialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void playMusic(View view) {
        startActivity(new Intent(DashBoard.this, AudioActivity.class));
    }

    public void loadVideo(View view) {

     /*   AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);

        builder.setTitle("No Videos Available");
        AlertDialog dialog = builder.create();
        dialog.show();*/

        startActivity(new Intent(this, VideoList.class));
    }

    public boolean checkInternetConnection() {

        //initialize connectivityManager to get the statuses of connectivity services
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        //connectivityManager have statuses of connection services
        if (connectivityManager != null) {

            //get the status of mobile data
            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //get status of wifi
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        //mobile data or wifi is connected
        //exit
        return (mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkInternetConnection())
            startActivity(new Intent(this, NoInternet.class));
    }

    public void gallery(View view) {

        startActivity(new Intent(this, Gallery.class));
    }

    public void newsLetters(View view) {
        startActivity(new Intent(this, NewsLetters.class));
    }

    public void more(View view) {
        startActivity(new Intent(this, DashBoard2.class));
    }
}
