package com.example.spiritualtablet.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.spiritualtablet.R;
import com.example.spiritualtablet.about.Founder;
import com.example.spiritualtablet.about.History;
import com.example.spiritualtablet.about.Introduction;
import com.example.spiritualtablet.about.Outlets;
import com.example.spiritualtablet.about.Principle;
import com.example.spiritualtablet.about.PyramidDoctors;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void introduction(View view) {

        startActivity(new Intent(this, Introduction.class));
    }

    public void history(View view) {

        startActivity(new Intent(this, History.class));
    }

    public void principle(View view) {

        startActivity(new Intent(this, Principle.class));
    }

    public void pyramidDoctors(View view) {

        startActivity(new Intent(this, PyramidDoctors.class));
    }

    public void outlets(View view) {

        startActivity(new Intent(this, Outlets.class));
    }

 /*   public void organisation(View view) {

        startActivity(new Intent(this, Organisation.class));
    }*/

    public void founder(View view) {
        startActivity(new Intent(this, Founder.class));
    }
}
