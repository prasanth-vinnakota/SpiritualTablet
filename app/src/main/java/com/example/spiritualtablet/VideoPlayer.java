package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.video_player);

        MediaController mediaController = new MediaController(this);

        videoView.setMediaController(mediaController);

        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spiritualtablet-b507c.appspot.com/o/Videos%2FVID_234020510_080531_843.mp4?alt=media&token=8f75e968-a64d-4043-b747-1f5e12ea05aa");
        videoView.setVideoURI(uri);
        videoView.start();
    }
}
