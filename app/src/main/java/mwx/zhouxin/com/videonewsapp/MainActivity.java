package mwx.zhouxin.com.videonewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mwx.zhouxin.com.videoplayer.full.full.VideoViewActivity;

public class MainActivity extends AppCompatActivity {

    private  Button btnLocal,btnLikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        btnLocal = (Button) findViewById(R.id.btnLocal);
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoViewActivity.open(MainActivity.this,VideoUrlRes.getTestVideo1());


            }
        });
        btnLikes = (Button) findViewById(R.id.btnLikes);
        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,PartPlayActivity.class);
                startActivity(intent);
            }
        });
    }

  /*  @Override
    public void onClick(View view) {
        Log.i("OnClick","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      /VideoViewActivity.open(this,VideoUrlRes.getTestVideo1());
        Intent intent= new Intent(this,PartPlayActivity.class);
        startActivity(intent);
    }*/
}
