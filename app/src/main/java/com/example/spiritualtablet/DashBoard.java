package com.example.spiritualtablet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class DashBoard extends AppCompatActivity {

    Button logOut;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
                startActivity(intent);
                finish();
            }
        });

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

                startActivity(new Intent(DashBoard.this, TeluguBooksList.class));
            }
        });

        builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(DashBoard.this, EnglishBooksList.class));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void playMusic(View view) {
        startActivity(new Intent(DashBoard.this,AudioActivity.class));
    }

    public void about(View view) {

        startActivity(new Intent(this, About.class));
    }

    public void loadVideo(View view) {

        startActivity(new Intent(this, LoadVideo.class));
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

    public void donate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);

        builder.setTitle("Through");
        builder.setMessage("UPI : something@somebank\nPhone pe : 0000000000\nGoogle pay : 0000000000\nAccount no : 0000000000\nIFSC CODE : BANK0000000\nBranch : Somewhere");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void contactUs(View view) {

        startActivity(new Intent(this, ContactUs.class));
    }
}
