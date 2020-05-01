package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AudioActivity extends AppCompatActivity {

    List<DataItem> lstDataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        lstDataItem = new ArrayList<>();


        lstDataItem.add(new DataItem("Dhyanam", "Audios", R.drawable.dhyanam));
        lstDataItem.add(new DataItem("Nissahayata", "Audios", R.drawable.nissahayatha));
        lstDataItem.add(new DataItem("Arishadvargalu", "Audios", R.drawable.arishadvargaalu));
        lstDataItem.add(new DataItem("Aadhyathmika Putrulu", "Audios", R.drawable.adyatmika_putrulu));
        lstDataItem.add(new DataItem("Aacharya Saangatyam", "Audios", R.drawable.acharya_sangatyam));
        lstDataItem.add(new DataItem("Nalugu Setruvulu", "Audios", R.drawable.nalugu_setruvulu));
        lstDataItem.add(new DataItem("Manchi Kashtalu", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Jeevitha Dhyeyam", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 1", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 2", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 3", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 4", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 4", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("G K Sir Speech", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Aathma Kutumba Dharmam", "Audios", R.drawable.audio_cover_page));


        RecyclerView myrv = findViewById(R.id.audio_recycler_view_id);
        AudioRecyclerViewAdapter myAdapter = new AudioRecyclerViewAdapter(getApplicationContext(),lstDataItem);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        myrv.setAdapter(myAdapter);


      /*  audioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Toast.makeText(getApplicationContext(),"Loading "+audioFileList.get(position),Toast.LENGTH_LONG).show();

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Songs").child("" + position);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue(String.class);
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
        }); */
    }
}

