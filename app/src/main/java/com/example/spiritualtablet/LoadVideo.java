package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class LoadVideo extends YouTubeBaseActivity {

    private final String API_KEY = "AIzaSyDRb2elgVqafrrzsU0lx0QIQ_gbqYA8E04";
    ListView youtubeVideoList;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener initializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_video);

        youTubePlayerView = findViewById(R.id.youtube_view);

        youtubeVideoList = findViewById(R.id.youtube_video_list);

        ArrayList<String> videoList = new ArrayList<>();

        videoList.add("Importance of Pyramid Meditation, by Dr. Gopala Krishna");
        videoList.add("The Celestine Prophecy, by James Redfield");

        ArrayAdapter videoListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, videoList);

        youtubeVideoList.setAdapter(videoListAdapter);



        youtubeVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                youTubePlayerView.setVisibility(View.VISIBLE);

                initializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                        if (position == 0){
                            youTubePlayer.loadVideo("cyr5-XBe4WA&feature=emb_logo");
                        }else if (position == 1){
                            youTubePlayer.loadVideo("sx-dWsD3UPg&feature=emb_logo");
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        Toast.makeText(getApplicationContext(),"Unable to play video, please try after sometime",Toast.LENGTH_LONG).show();
                    }
                };

                youTubePlayerView.initialize(API_KEY,initializedListener);

                initializedListener = null;
            }
        });

    }
}
