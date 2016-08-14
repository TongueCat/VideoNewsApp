package mwx.zhouxin.com.videoplayer.full.full;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import io.vov.vitamio.widget.MediaController;
import mwx.zhouxin.com.videoplayer.R;

/**
 * Created by Administrator on 2016/8/11.
 */
/*
* 继承 Mediacontroller 实现自定义的视频播放器控制器
* 1重新实现视图view
* 2加入一些功能
*
* MediaController自己是有视图和功能的
*
* 我们重写他的视图后，可以不改变控件 ID号,就可以直接使用本来的功能
* 如seekbar来改变播放进度,Button来暂停和播放视频,当前时间，总时间，视频标题,drawble图像
*
*
* */
public class CustomMediaController extends MediaController{

    private  MediaPlayerControl mediaPlayerControl;

    //用来调整音量的
    private   AudioManager audioManager;

    private Window window;//用来调整亮度的

    //最大音量
    private  int maxVolume;

    //当前音量
    private  int currentVolume;
    //当前亮度
    private  float currentBrightness;
    //设置最大亮度
   private float maxBrightness=1.0F;
    //设置最小亮度
   private float minBrightness=0.01f;


    public CustomMediaController(Context context) {
        super(context);
        //获取系统
        audioManager=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume =audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        window = ((Activity)context).getWindow();



    }

    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写这个方法(vitamio Mediacontroller的)来自定义MediaController

    @Override
    protected View makeControllerView() {

        View view =LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller,this);

        initView(view);

        return view;
    }
    //父类的MediaPlayerControl是私有的，重写这个方法，就是为了将player保存一份，方便我们使用
    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        this.mediaPlayerControl=player;
    }

    private  void initView(View view){
        //设置forward快进
        ImageButton btnFastForward= (ImageButton) view.findViewById(R.id.btnFastForward);
        btnFastForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            long positon= mediaPlayerControl.getCurrentPosition();
                positon +=10000;
                mediaPlayerControl.seekTo(positon);
            }
        });
        //设置rewind快退
        ImageButton btnFastRewid= (ImageButton) view.findViewById(R.id.btnFastRewind);
        btnFastForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                long positon= mediaPlayerControl.getCurrentPosition();
                positon +=10000;
                mediaPlayerControl.seekTo(positon);
            }
        });

        //调整屏幕亮度(左边)和音量(右边)
        //1拿到VIEe(整个视频播放区我们故意放一个空的VIE我）
//       //2 对VIEe进行TOUch监听
        //3在touch监听里用手势处理
        //4完成屏幕左侧和右侧的判断
        //5完成我们的业务
        final View adjustView= view.findViewById(R.id.adjustView);
        final GestureDetector gestureDetector= new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                //scroll起始位置
                float startX= e1.getX();
                float startY= e1.getY();
                float endX= e2.getX();
                float endY= e2.getY();

                //判断是左侧不是右侧
                float width= adjustView.getWidth();
                float height= adjustView.getHeight();

                //判断手势在屏幕上下滑动的百分比
                float percentage= (startY -endY)/height;


               // 如果是在屏幕左侧的1/5,调亮度
                if(startX < width/5){
                    adjustBrightness(percentage);
                    return true;
                }
                //如果是在屏幕右侧的1/5，调音量
                else  if (startX >width * 4/5){
                    adjustVolume(percentage);
                    return  true;
                }
       
                
                
                
                return true;
            }
        });

        //对view进行touch监听,但我们自己不去判断各种touch动作（我们用系统提供的手势处理）
        adjustView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //按下事件时（也代码马上将开始手势处理）获取当前音量及亮度
                //使用ACTION_MASK是为了过滤多点触屏事件
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    currentBrightness=window.getAttributes().screenBrightness;

                }
               gestureDetector.onTouchEvent(motionEvent);
                //为了在调整过程中，不消失
                CustomMediaController.this.show();
                return true;
            }
        });


    }


    //亮度调节
    private void adjustBrightness(float percentage) {
        //计算出目标亮度
        float targeBrightness= (float) (percentage * maxBrightness +currentBrightness);
        targeBrightness = targeBrightness >maxBrightness?maxBrightness : targeBrightness;
        targeBrightness = targeBrightness <  minBrightness ?  minBrightness : targeBrightness;

        WindowManager.LayoutParams layoutParams= window.getAttributes();
        layoutParams.screenBrightness=targeBrightness;
        window.setAttributes(layoutParams);
    }

    //音量调节
    private void adjustVolume(float percentage) {
        //计算出目标音量
        int targeVolume= (int) (percentage * maxVolume +currentVolume);
        targeVolume = targeVolume >maxVolume?maxVolume : targeVolume;
        targeVolume =targeVolume < 0 ? 0 : targeVolume;
        //设置音量

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,targeVolume,AudioManager.FLAG_SHOW_UI);

    }
}
