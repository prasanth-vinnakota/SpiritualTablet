package com.example.spiritualtablet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class VideoList extends YouTubeBaseActivity {

    ListView videoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        videoListView = findViewById(R.id.video_list);

        ArrayList<String> videoList = new ArrayList<>();

        videoList.add("Importance of Pyramid Meditation, by Dr. Gopala Krishna");
        videoList.add("The Celestine Prophecy, by James Redfield");

        ArrayAdapter videoListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, videoList);

        videoListView.setAdapter(videoListAdapter);



        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent = new Intent(VideoList.this, VideoPlayer.class);
                startActivity(intent);
            }
        });

    }
}
