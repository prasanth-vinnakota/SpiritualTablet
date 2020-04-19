package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    CardView cardView;
    TextView message, message2;

    FirebaseAuth mAuth;

    DatabaseReference db_ref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        cardView = findViewById(R.id.buy_premium);
        message = findViewById(R.id.payable_amount);
        message2 = findViewById(R.id.payment_desc);

        mAuth = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            db_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            Map<String, Object> map = (Map<String,Object>)dataSnapshot.getValue();

                            if (map == null)
                                return;

                            if (map.get("premium") != null){

                                if (Objects.requireNonNull(map.get("premium")).toString().equals("no")){

                                    db_ref.child("premium").setValue("yes");
                                    message.setText("Premium User");
                                    message2.setText("");
                                }else {
                                    Toast.makeText(getApplicationContext(),"You are a premium user",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            }
        });
    }
}
