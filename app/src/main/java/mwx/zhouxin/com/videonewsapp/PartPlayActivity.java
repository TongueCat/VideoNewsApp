package mwx.zhouxin.com.videonewsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mwx.zhouxin.com.videoplayer.full.full.part.SimplerVideoView;

public class PartPlayActivity extends AppCompatActivity {
   private SimplerVideoView simplerVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_play);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        simplerVideoView= (SimplerVideoView) findViewById(R.id.simpleVideoPlayer);
        simplerVideoView.setVideoPath(VideoUrlRes.getTestVideo1());
    }

    @Override
    protected void onResume() {
        super.onResume();
        simplerVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simplerVideoView.onPasuse();
    }
}
