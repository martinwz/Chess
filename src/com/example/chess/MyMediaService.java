package com.example.chess;
import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.media.MediaPlayer;  
public class MyMediaService extends Service implements MediaPlayer.OnCompletionListener{  
    MediaPlayer myPlayer;  
    String data=null;
    private final IBinder binder = new AudioBinder();  
    @Override  
    public IBinder onBind(Intent arg0) {  
        // TODO Auto-generated method stub  
        return binder;  
    }  
    /** 
     * 当Audio播放完的时候触发该动作 
     */  
    @Override  
    public void onCompletion(MediaPlayer player) {  
        // TODO Auto-generated method stub  
        stopSelf();//结束了，则结束Service  
    }  
    //实例化MediaPlayer对象  
    public void onCreate(){  
        super.onCreate();  
        //我们从raw文件夹中获取一个应用自带的mp3文件  
       myPlayer=new MediaPlayer();
        myPlayer.setOnCompletionListener(this);  
        myPlayer.setLooping(true);//设置单曲循环
    }  
    public int onStartCommand(Intent intent, int flags, int startId){  
    	data=intent.getStringExtra("sp");
    	String currentSong=data.substring(77).replace(".mp3", "");
    	Toast.makeText(getApplicationContext(),"当前播放的是：" 
    	+currentSong, Toast.LENGTH_SHORT).show();
    	if(myPlayer.isPlaying()){
    		myPlayer.stop();
    		myPlayer.release();
    		try {
				myPlayer.setDataSource(data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		try {
				myPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		try {
				myPlayer.setDataSource(data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				myPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     //准备
    	}
        	myPlayer.start(); //播放
        return START_STICKY;  
    }  
    public void onDestroy(){  //停止音乐
        super.onDestroy();  
        if(myPlayer.isPlaying()){  
            myPlayer.stop();  
        }  
        myPlayer.release();  
    }  
    //为了和Activity交互，我们需要定义一个Binder对象  
    class AudioBinder extends Binder{  
        //返回Service对象  
        MyMediaService getService(){  
            return MyMediaService.this;  
        }  
    }  
}  