package com.example.spiritualtablet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spiritualtablet.dashboard.About;
import com.example.spiritualtablet.dashboard.ContactUs;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoard2 extends AppCompatActivity {

    Button logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board2);

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

    public void about(View view) {

        startActivity(new Intent(this, About.class));
    }

    public void donate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard2.this);

        builder.setTitle("Through");
        builder.setMessage("ICICI Bank A/c No - 006001034726\nIFSC Code - ICIC0000060\nBranch - Dwaraka Nagar\n\nAndhra Bank A/c No - 035111100002555\n IFSC Code - ANDB0000351\nBranch - Maharanipeta");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void contactUs(View view) {

        startActivity(new Intent(this, ContactUs.class));
    }

    public void dailyTip(View view) {

        startActivity(new Intent(this, DailyTip.class));
    }

    public void blog (View view){

        startActivity(new Intent(this, Home.class));
    }
}
