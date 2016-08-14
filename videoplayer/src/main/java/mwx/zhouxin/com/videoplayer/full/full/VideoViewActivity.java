package mwx.zhouxin.com.videoplayer.full.full;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import mwx.zhouxin.com.videoplayer.R;

public class VideoViewActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
/*
* 使用videoview进行视频播放的ACtivity，其中MediaController是自定义的
*
* 使用open方法传入视频path,启动activity
* */
    private static  final  String KEY_VIDEO_PATH ="KEY_VIDEO_PATH";

    public  static  void  open(Context context,String videoPath){
        Log.i("open","!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Intent intent = new Intent(context,VideoViewActivity.class);
        intent.putExtra(KEY_VIDEO_PATH,videoPath);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置窗口的背景颜色
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        setContentView(R.layout.activity_video_view);


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //1我们要初始化视图
        ininBufferViews();
        //2,我们要初始化VideoView
        initVideoView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if(pm.isScreenOn()) {
            videoView.setVideoPath(getIntent().getStringExtra(KEY_VIDEO_PATH));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止播放
        videoView.stopPlayback();

    }

    private MediaPlayer mediaPlayer;
    private VideoView videoView;
//缓存信息（图像）
    private ImageView ivLoading;
    //缓冲信息(文本信息 )
    private TextView tvBufferInfo;
    //缓存熟读
    private  int downloadSpeed;
    // 当前缓冲百分比
    private int bufferPercent;

    private void initVideoView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        //初始化
        Vitamio.isInitialized(this);
        /*videoView.setVideoPath();
        videoView.start();*/
        //播放 暂停 快进
       // videoView.setMediaController(new MediaController(this));
        videoView.setMediaController(new CustomMediaController(this));
        videoView.setKeepScreenOn(true);
        videoView.requestFocus();

        //资源准备监听处理
        videoView.setOnPreparedListener(this);
        //状态信息监听
        videoView.setOnInfoListener(this);
        //缓冲更新监听
        videoView.setOnBufferingUpdateListener(this);
    }
    private  void ininBufferViews(){
        tvBufferInfo = (TextView) findViewById(R.id.tvBufferInfo);
        ivLoading= (ImageView) findViewById(R.id.ivLoading);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        //设置缓冲区大小（缓冲区填充完后，才开始播放），默认值是1M
        mediaPlayer.setBufferSize(512*1024);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            //开始缓存
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                startBuffer();

                break;
            //结束缓存
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                endBuffer();
                break;
           //缓存时下载的速率
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadSpeed= extra;
                    updateBufferViewInfo();
                break;


        }

        return true;
    }




// 开始缓存
    private void startBuffer() {
        if(videoView.isPlaying()){
            videoView.pause();
        }
        tvBufferInfo.setVisibility(View.VISIBLE);
        ivLoading.setVisibility(View.VISIBLE);
        downloadSpeed= 0;
        bufferPercent=0;
    }
    //结束缓存
    private void endBuffer() {
        //开始播放
        tvBufferInfo.setVisibility(View.GONE);
        ivLoading.setVisibility(View.GONE);
        videoView.start();

    }


    //缓存更新监听
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //percent当前缓冲的百分比
        bufferPercent=percent;
        updateBufferViewInfo();
    }
    //缓冲时，速度变化时调用的
    private void updateBufferViewInfo() {
        //  String s= bufferPercent+"%"+" "+downloadSpeed
        String info = String.format(Locale.CHINA, "%d%%,%dkb/s", bufferPercent, downloadSpeed);
        tvBufferInfo.setText(info);
    }
}
