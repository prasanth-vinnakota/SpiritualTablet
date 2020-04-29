package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {

    ListView audioList;
    ArrayList<String> audioFileList;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        audioList = findViewById(R.id.audio_list);

        audioFileList = new ArrayList<>();

        audioFileList.add("Nissahayata");
        audioFileList.add("Manchi Kashtalu");
        audioFileList.add("Jeevitha Dhyeyam");
        audioFileList.add("Garbaasayam-1");
        audioFileList.add("Garbaasayam-2");
        audioFileList.add("Garbaasayam-3");
        audioFileList.add("Garbaasayam-4");
        audioFileList.add("G.K Sir Speech");
        audioFileList.add("Dhyanam");
        audioFileList.add("Aathma Kutumba Dharmam");
        audioFileList.add("Arishadvargalu");
        audioFileList.add("Aadhyathmika Putrulu");
        audioFileList.add("Aacharya Saangatyam");
        audioFileList.add("Nalugu Setruvulu");

        ArrayAdapter audioListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, audioFileList);

        audioList.setAdapter(audioListAdapter);

        audioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Toast.makeText(getApplicationContext(),"Loading "+audioFileList.get(position),Toast.LENGTH_LONG).show();

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child("" + position);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        url = dataSnapshot.getValue(String.class);
                        Intent i = new Intent(AudioActivity.this, Player.class);
                        i.putExtra("url",url);
                        i.putExtra("song_name", audioFileList.get(position));
                        startActivity(i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}

