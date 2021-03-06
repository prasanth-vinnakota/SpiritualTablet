package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DailyTip extends AppCompatActivity {

    TextView title, description;
    ImageView image;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tip);

        //initializing Progress bar
        mProgressBar = findViewById(R.id.daily_tip_progressbar);
        //initializing text view
        title = findViewById(R.id.daily_tip_title);
        description = findViewById(R.id.daily_tip_description);

        //initializing image view
        image = findViewById(R.id.daily_tip_image);

        final Date date = new Date();


        //getting database reference
        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("Daily Tip");

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.getKey().equals(timestampToString(date.getTime()))){


                            //get data from firebase
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                            if (map == null)
                                return;

                            if (map.get("Title") != null){
                                title.setText(Objects.requireNonNull(map.get("Title")).toString());
                                title.setVisibility(View.VISIBLE);
                            }

                            if (map.get("Image") != null){

                                Glide.with(getApplicationContext()).load(map.get("Image")).into(image);
                                image.setVisibility(View.VISIBLE);
                            }

                            if (map.get("Description") != null){

                                description.setText(Objects.requireNonNull(map.get("Description")).toString());
                                description.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String val = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return val;


    }

}
